package org.example.financial_transaction.service.impl;

import org.example.financial_transaction.dao.customerRepository.CustomerRepository;
import org.example.financial_transaction.exception.DuplicateException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.service.IAccountService;
import org.example.financial_transaction.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository repository;
    private final IAccountService iAccountService;

    public CustomerServiceImpl(CustomerRepository repository, IAccountService iAccountService) {
        this.repository = repository;
        this.iAccountService = iAccountService;
    }

    //todo:exceptions should be spring message
    @Transactional
    @Override
    public void registerCustomer(Customer customer) {
        Optional<Customer> existingCustomer = repository.findByNationalCode(customer.getNationalCode());
        existingCustomer.ifPresent(c -> {
            throw new DuplicateException(c.getNationalCode());
        });
        Account account = iAccountService.createAccount();
        customer.setAccount(account);
        repository.save(customer);
    }
}

