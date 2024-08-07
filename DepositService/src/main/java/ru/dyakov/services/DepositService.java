package ru.dyakov.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

@Service
@Slf4j
public class DepositService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private CurrentRequestRepository currentRequestRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Iterable<Deposit> getDeposits(int customerId) {
        return depositRepository.findByBankAccountId(customerId);
    }

    // Получить отклоненные заявки
    public Iterable<CurrentRequestStatus> getRejectedRequests() {
        return currentRequestRepository.findByRequestStatusId(StatusTypes.REJECTED.id);
    }

    // Создание заявки
    public Integer createRequest(int customerId) {
        Request request = requestRepository.insertRequest(customerId, new Date());
        log.info("Создана заявка на открытие вклада №{}", request.getId());
        return request.getId();
    }

    // Открытие вклада
    public Integer createDeposit(String jwt, Ticket ticket) {
        String token = jwt.substring(7);
        int customerId = jwtService.getCustomerIdFromJwtToken(token);
        Customer customer = getCustomerInfo(jwt);

        if (customer.getBankAccount().getAmount().compareTo(ticket.getAmount()) < 0) {
            createRejectedRequest(ticket.getRequestId());
            return -1;
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
        return deposit.getId();
    }

    private Customer getCustomerInfo(String jwt) {
        RestTemplate restTemplate = new RestTemplate();

        String urlCustomer = "http://localhost:8082/api/accounts/getCustomerInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Customer> response = restTemplate.exchange(urlCustomer, HttpMethod.GET, entity, Customer.class);
        return response.getBody();
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
