package ru.dyakov.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dyakov.entities.Customer;
import ru.dyakov.exceptions.WrongSumException;
import ru.dyakov.repositories.CustomersRepository;
import ru.dyakov.responses.WithdrawResponse;
import ru.dyakov.utilties.JwtService;

import java.math.BigDecimal;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomersRepository customersRepository;

    public BigDecimal makeDeposit(int customerId, BigDecimal addition) throws WrongSumException {
        Customer customer = customersRepository.findById(customerId).orElseThrow();
        BigDecimal amount = customer.getBankAccount().getAmount();
        BigDecimal sum = amount.add(addition);

        if (addition.signum() < 0) {
            throw new WrongSumException();
        }

        customer.getBankAccount().setAmount(sum);
        customersRepository.save(customer);
        log.info("Счет {} пополнен на {}", customer.getBankAccount().getNumBankAccounts(), addition);
        return sum;
    }

    public BigDecimal makeWithdraw(int customerId, BigDecimal substraction) throws WrongSumException {
        Customer customer = customersRepository.findById(customerId).orElseThrow();
        BigDecimal amount = customer.getBankAccount().getAmount();

        if (substraction.signum() < 0 || amount.compareTo(substraction) < 0) {
            throw new WrongSumException();
        }

        BigDecimal sum = amount.subtract(substraction);

        customer.getBankAccount().setAmount(sum);
        customersRepository.save(customer);
        log.info("С счета {} снято {}", customer.getBankAccount().getNumBankAccounts(), substraction);
        return sum;
    }
}
