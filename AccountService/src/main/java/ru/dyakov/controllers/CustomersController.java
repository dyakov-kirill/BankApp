package ru.dyakov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dyakov.requests.WithdrawRequest;
import ru.dyakov.responses.DepositResponse;
import ru.dyakov.responses.WithdrawResponse;
import ru.dyakov.utilties.JwtService;
import ru.dyakov.entities.Customer;
import ru.dyakov.repositories.CustomersRepository;
import ru.dyakov.requests.DepositRequest;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class CustomersController {

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/getCustomerInfo")
    private ResponseEntity<Customer> getCustomer(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                return ResponseEntity.ok(customersRepository.findById(customerId).orElseThrow());
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/deposit")
    private ResponseEntity<DepositResponse> deposit(@RequestHeader(name="Authorization") String jwt, @RequestBody DepositRequest request) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                BigDecimal addition = request.getValue();
                Customer customer = customersRepository.findById(customerId).orElseThrow();
                BigDecimal amount = customer.getBankAccount().getAmount();
                BigDecimal sum = amount.add(addition);

                if (addition.signum() < 0) {
                    return ResponseEntity.badRequest().body(new DepositResponse(null));
                }

                customer.getBankAccount().setAmount(sum);
                customersRepository.save(customer);
                log.info("Счет {} пополнен на {}", customer.getBankAccount().getNumBankAccounts(), addition);
                return ResponseEntity.ok(new DepositResponse(sum));
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/withdraw")
    private ResponseEntity<WithdrawResponse> deposit(@RequestHeader(name="Authorization") String jwt, @RequestBody WithdrawRequest request) {
        String token = jwt.substring(7);
        try {
            if (jwtService.validateJwt(token) && !jwtService.jwtIsExpired(token)) {
                int customerId = jwtService.getCustomerIdFromJwtToken(token);
                BigDecimal substraction = request.getValue();

                Customer customer = customersRepository.findById(customerId).orElseThrow();
                BigDecimal amount = customer.getBankAccount().getAmount();
                if (substraction.signum() < 0 || amount.compareTo(substraction) < 0) {
                    return ResponseEntity.badRequest().body(new WithdrawResponse(null));
                }
                BigDecimal sum = amount.subtract(substraction);

                customer.getBankAccount().setAmount(sum);
                customersRepository.save(customer);
                log.info("С счета {} снято {}", customer.getBankAccount().getNumBankAccounts(), substraction);
                return ResponseEntity.ok(new WithdrawResponse(sum));
            } else {
                return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
        }

    }
}
