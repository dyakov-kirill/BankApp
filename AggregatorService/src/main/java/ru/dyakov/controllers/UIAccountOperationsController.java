package ru.dyakov.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.dyakov.requests.DepositRequest;
import ru.dyakov.requests.WithdrawRequest;
import ru.dyakov.responses.DepositResponse;
import ru.dyakov.responses.WithdrawResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Controller
@RequestMapping("/")
public class UIAccountOperationsController {


    // Страница с пополнением и снятием денег
    @GetMapping("/account_operations")
    public String homepage(Model model) {
        return "account_operations";
    }

    // Запрос на пополнение счета
    @PostMapping("/deposit")
    public String deposit(Model model, HttpServletRequest request, @RequestParam("amount") String amount) {
        BigDecimal numAmount = new BigDecimal(amount);

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8082/api/accounts/deposit";
            String jwt = (String) request.getSession().getAttribute("Authorization");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);

            DepositRequest depositRequest = new DepositRequest(numAmount);
            HttpEntity<DepositRequest> entity = new HttpEntity<>(depositRequest, headers);

            ResponseEntity<DepositResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, DepositResponse.class);
            DepositResponse depositResponse = response.getBody();

            if (depositResponse != null && depositResponse.getValue() != null) {
                model.addAttribute("message", "Операция успешна. Текущее состояние счета: " + depositResponse.getValue());
            } else {
                model.addAttribute("message", "Операция неуспешна");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Операция неуспешна");
        }
        return "account_operations";
    }

    // Запрос для снятия денег
    @PostMapping("/withdraw")
    public String withdraw(Model model, HttpServletRequest request, @RequestParam("amount") String amount) {
        BigDecimal numAmount = new BigDecimal(amount);

        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://localhost:8082/api/accounts/withdraw";
            String jwt = (String) request.getSession().getAttribute("Authorization");

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", jwt);

            WithdrawRequest withdrawRequest = new WithdrawRequest(numAmount);
            HttpEntity<WithdrawRequest> entity = new HttpEntity<>(withdrawRequest, headers);

            ResponseEntity<WithdrawResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, WithdrawResponse.class);
            WithdrawResponse withdrawResponse = response.getBody();

            if (withdrawResponse != null && withdrawResponse.getValue() != null) {
                model.addAttribute("message", "Операция успешна. Текущее состояние счета: " + withdrawResponse.getValue());
            } else {
                model.addAttribute("message", "Операция неуспешна");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Операция неуспешна");
        }
        return "account_operations";
    }
}
