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
import ru.dyakov.services.DepositService;
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

    @Autowired
    private DepositService depositService;

    // Получить список всех вкладов
    @GetMapping("/getDeposits")
    private ResponseEntity<Iterable<Deposit>> getDeposits(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        int customerId = jwtService.getCustomerIdFromJwtToken(token);
        Iterable<Deposit> result = depositService.getDeposits(customerId);
        return ResponseEntity.ok(result);
    }

    // Получить отклоненные заявки
    @GetMapping("/getRejectedRequests")
    private ResponseEntity<Iterable<CurrentRequestStatus>> getRejectedRequests(@RequestHeader(name="Authorization") String jwt) {
        Iterable<CurrentRequestStatus> result = depositService.getRejectedRequests();
        return ResponseEntity.ok(result);
    }

    // Создание заявки
    @PostMapping("/createRequest")
    private ResponseEntity<Integer> createRequest(@RequestHeader(name="Authorization") String jwt) {
        String token = jwt.substring(7);
        int customerId = jwtService.getCustomerIdFromJwtToken(token);
        Integer requestId = depositService.createRequest(customerId);
        return ResponseEntity.ok(requestId);
    }

    // Открытие вклада
    @PostMapping("/createDeposit")
    private ResponseEntity<Integer> createDeposit(@RequestHeader(name="Authorization") String jwt, @RequestBody Ticket ticket) {
        int result = depositService.createDeposit(jwt, ticket);
        return ResponseEntity.ok(result);
    }
}
