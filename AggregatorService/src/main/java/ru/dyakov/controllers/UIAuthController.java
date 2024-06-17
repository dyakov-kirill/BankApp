package ru.dyakov.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import ru.dyakov.entities.Customer;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UIAuthController {

    // Страница входа
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // Главная страница с состоянием счета и навигацией
    @GetMapping("/homepage")
    public String homepage(HttpServletRequest request, Model model) {
        String jwt = (String) request.getSession().getAttribute("Authorization");
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8082/api/accounts/getCustomerInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Customer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Customer.class);
        Customer actualCustomer = response.getBody();

        model.addAttribute("phoneNumber", actualCustomer.getPhoneNumber());
        model.addAttribute("balance", actualCustomer.getBankAccount().getAmount().toString());
        model.addAttribute("bankAccountNumber", actualCustomer.getBankAccount().getNumBankAccounts());
        return "homepage";
    }
}
