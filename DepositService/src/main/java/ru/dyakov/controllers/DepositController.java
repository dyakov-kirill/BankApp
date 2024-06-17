package ru.dyakov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.dyakov.entities.CurrentRequestStatus;
import ru.dyakov.entities.Customer;
import ru.dyakov.entities.Deposit;
import ru.dyakov.entities.Request;
import ru.dyakov.enumerations.StatusTypes;
import ru.dyakov.repositories.CurrentRequestRepository;
import ru.dyakov.repositories.DepositRepository;
import ru.dyakov.repositories.RequestRepository;
import ru.dyakov.requests.Ticket;
import ru.dyakov.requests.WithdrawRequest;
import ru.dyakov.responses.WithdrawResponse;
import ru.dyakov.utilties.JwtService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/deposits")
@Slf4j
public class DepositController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private CurrentRequestRepository currentRequestRepository;

    @Autowired
    private RequestRepository requestRepository;

    @GetMapping("/getDeposits")
    private ResponseEntity<Iterable<Deposit>> getDeposits(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                List<Deposit> result = depositRepository.findByBankAccountId(customerId);

                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/getRejectedRequests")
    private ResponseEntity<Iterable<CurrentRequestStatus>> getRejectedRequests(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                List<CurrentRequestStatus> result = currentRequestRepository.findByRequestStatusId(StatusTypes.REJECTED.id);
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/createRequest")
    private ResponseEntity<Integer> createRequest(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                Request request = requestRepository.insertRequest(customerId, new Date());
                log.info("Создана заявка на открытие вклада №{}", request.getId());
                return ResponseEntity.ok(request.getId());
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/createDeposit")
    private ResponseEntity<Integer> createDeposit(@RequestHeader(name="Authorization") String jwt, @RequestBody Ticket ticket) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                Customer customer = getCustomerInfo(jwt);

                if (customer.getBankAccount().getAmount().compareTo(ticket.getAmount()) < 0) {
                    createRejectedRequest(ticket.getRequestId());
                    return ResponseEntity.ok(-1);
                }

                boolean refill = ticket.getDepositType() != 3;


                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusMonths(ticket.getDepositTerm());
                LocalDate paymentDate = startDate.plusMonths(1);
                BigDecimal rate = BigDecimal.valueOf(11);
                withdrawFromAccount(jwt, ticket.getAmount());
                Deposit deposit = depositRepository.createDeposit(customerId, ticket.getDepositType(), refill, ticket.getAmount(),
                                                startDate, endDate, rate, ticket.getInterestPayment(),
                                                customerId, paymentDate, ticket.isCapitalization(), customerId);
                log.info("Открыт вклад №{}", deposit.getId());
                return ResponseEntity.ok(deposit.getId());
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    private Customer getCustomerInfo(String jwt) {
        RestTemplate restTemplate = new RestTemplate();

        String urlCustomer = "http://localhost:8082/api/accounts/getCustomerInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Customer> response = restTemplate.exchange(urlCustomer, HttpMethod.GET, entity, Customer.class);
        Customer customer = response.getBody();
        return customer;
    }

    private void withdrawFromAccount(String jwt, BigDecimal amount) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8082/api/accounts/withdraw";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        WithdrawRequest withdrawRequest = new WithdrawRequest(amount);
        HttpEntity<WithdrawRequest> entity = new HttpEntity<>(withdrawRequest, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, WithdrawResponse.class);
    }

    @Transactional
    private void createRejectedRequest(int requestId) {
        currentRequestRepository.insertRequest(requestId, StatusTypes.REJECTED.id, new Date());
    }
}
