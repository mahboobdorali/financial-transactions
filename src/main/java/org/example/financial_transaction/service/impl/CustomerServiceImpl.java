package org.example.financial_transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.customerRepository.CustomerRepository;
import org.example.financial_transaction.exception.DuplicateException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.dto.CustomerUpdateRequest;
import org.example.financial_transaction.service.IAccountService;
import org.example.financial_transaction.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository repository;
    private final IAccountService iAccountService;

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

    @Override
    @Transactional
    public void update(CustomerUpdateRequest customerUpdateRequest) {
        if (customerUpdateRequest.customerId() != null) {
            Customer prevCustomer = findById(customerUpdateRequest.customerId());

//            Optional.ofNullable(customerUpdateRequest.name()).ifPresent(newName->{
//                    History history = new History();
//                    history.setDescription();
//                    prevCustomer.setName(newName);
//            });

            Optional.ofNullable(customerUpdateRequest.name()).ifPresent(prevCustomer::setName);
            Optional.ofNullable(customerUpdateRequest.nationalCode()).ifPresent(prevCustomer::setNationalCode);
            Optional.ofNullable(customerUpdateRequest.establishmentDate()).ifPresent(prevCustomer::setEstablishmentDate);
            Optional.ofNullable(customerUpdateRequest.customerType()).ifPresent(prevCustomer::setCustomerType);
            Optional.ofNullable(customerUpdateRequest.phoneNumber()).ifPresent(prevCustomer::setPhoneNumber);
            Optional.ofNullable(customerUpdateRequest.address()).ifPresent(prevCustomer::setAddress);
            repository.save(prevCustomer);
        }
        if (customerUpdateRequest.accountId() != null) {
            Account prevAccount = iAccountService.findById(customerUpdateRequest.accountId());
            Optional.ofNullable(customerUpdateRequest.accountType()).ifPresent(prevAccount::setAccountType);
            iAccountService.pureSave(prevAccount);
        }
    }

    @Override
    public Customer findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }
}

