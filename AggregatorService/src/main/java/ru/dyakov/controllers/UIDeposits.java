package ru.dyakov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.dyakov.entities.CurrentRequestStatus;
import ru.dyakov.entities.Customer;
import ru.dyakov.entities.Deposit;
import ru.dyakov.requests.Ticket;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping
@Slf4j
public class UIDeposits {

    // Страница со списком вкладов и заявок
    @GetMapping("/deposits")
    private String deposits(Model model, HttpServletRequest request) {
        String jwt = (String) request.getSession().getAttribute("Authorization");

        Customer customer = getCustomerInfo(jwt);
        List<Deposit> deposits = getDeposits(jwt);
        List<CurrentRequestStatus> rejectedRequests = getRejectedRequests(jwt);

        model.addAttribute("accountBalance", customer.getBankAccount().getAmount());

        model.addAttribute("minAmount", "10000");
        model.addAttribute("minPeriod", "1 месяц");
        model.addAttribute("maxRate", "11%");

        model.addAttribute("openDeposits", deposits);
        model.addAttribute("rejectedRequests", rejectedRequests);
        return "deposits";
    }

    // Страница с открытием вклада
    @GetMapping("/openDeposit")
    private String openDeposit(Model model, HttpServletRequest request) {
        String jwt = (String) request.getSession().getAttribute("Authorization");

        Customer customer = getCustomerInfo(jwt);
        BigDecimal bankAccount = customer.getBankAccount().getNumBankAccounts();

        Ticket ticket = new Ticket();
        ticket.setInterestPaymentAccount(bankAccount);
        ticket.setReturnAccount(bankAccount);
        ticket.setWithdrawalAccount(bankAccount);

        model.addAttribute("bankAccount", bankAccount);
        model.addAttribute("ticket", ticket);
        return "openDeposit";
    }

    // Запрос на создание заявки
    @PostMapping("/createRequest")
    private String createRequest(Model model, HttpServletRequest request, Ticket ticket) {
        String jwt = (String) request.getSession().getAttribute("Authorization");
        int requestId = createRequest(jwt);
        ticket.setRequestId(requestId);
        request.getSession().setAttribute("ticket", ticket);
        return "sms";
    }

    // Запрос на открытие вклада
    @PostMapping("/createDeposit")
    private String createDeposit(Model model, HttpServletRequest request) {
        Ticket ticket = (Ticket) request.getSession().getAttribute("ticket");
        String jwt = (String) request.getSession().getAttribute("Authorization");
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8083/api/deposits/createDeposit";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<Ticket> entity = new HttpEntity<>(ticket, headers);
        ResponseEntity<Integer> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, Integer.class);
        } catch (Exception e) {
            log.info("Вклад не был открыт");
        } finally {
            if (response == null || response.getBody() == null || response.getBody() == -1) {
                model.addAttribute("message", "Вклад не открыт. Недостаточно средств для списания.");
            } else {
                model.addAttribute("message", "Вклад успешно открыт.");
            }
        }

        return "result";
    }

    // Получение списка вкладов (микросервис депозитов)
    private List<Deposit> getDeposits(String jwt) {
        RestTemplate restTemplate = new RestTemplate();

        String urlCustomer = "http://localhost:8083/api/deposits/getDeposits";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(urlCustomer, HttpMethod.GET, entity, List.class);
        List<Deposit> deposits = response.getBody();
        return deposits;
    }

    // Получение списка отклоненных заявок (микросервис депозитов)
    private List<CurrentRequestStatus> getRejectedRequests(String jwt) {
        RestTemplate restTemplate = new RestTemplate();

        String urlCustomer = "http://localhost:8083/api/deposits/getRejectedRequests";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(urlCustomer, HttpMethod.GET, entity, List.class);
        List<CurrentRequestStatus> requests = response.getBody();
        return requests;
    }

    // Получение информации о клиенте (микросервис клиентов)
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

    // Создание заявки (микросервис депозитов)
    private int createRequest(String jwt) {
        RestTemplate restTemplate = new RestTemplate();

        String urlCustomer = "http://localhost:8083/api/deposits/createRequest";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Integer> response = restTemplate.exchange(urlCustomer, HttpMethod.POST, entity, Integer.class);
        return response.getBody();
    }
}
