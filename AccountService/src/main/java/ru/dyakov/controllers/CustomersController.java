package ru.dyakov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dyakov.entities.Customer;
import ru.dyakov.repositories.CustomersRepository;
import ru.dyakov.requests.DepositRequest;
import ru.dyakov.requests.WithdrawRequest;
import ru.dyakov.responses.DepositResponse;
import ru.dyakov.responses.WithdrawResponse;
import ru.dyakov.services.AccountService;
import ru.dyakov.utilties.JwtService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
public class CustomersController {

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountService accountService;

    // Получение информации о клиенте
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
        int customerId = jwtService.getCustomerIdFromJwtToken(token);
        BigDecimal addition = request.getValue();

        try {
            BigDecimal sum = accountService.makeDeposit(customerId, addition);
            return ResponseEntity.ok(new DepositResponse(sum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new DepositResponse(null));
        }
    }

    @PostMapping("/withdraw")
    private ResponseEntity<WithdrawResponse> deposit(@RequestHeader(name="Authorization") String jwt, @RequestBody WithdrawRequest request) {
        String token = jwt.substring(7);
        int customerId = jwtService.getCustomerIdFromJwtToken(token);
        BigDecimal subtraction = request.getValue();

        try {
            BigDecimal sum = accountService.makeWithdraw(customerId, subtraction);
            return ResponseEntity.ok(new WithdrawResponse(sum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new WithdrawResponse(null));
        }
    }
}
