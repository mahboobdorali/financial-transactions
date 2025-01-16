package org.example.financial_transaction.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.dao.CustomerRepository;
import org.example.financial_transaction.exception.DuplicateException;
import org.example.financial_transaction.model.Account;
import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.History;
import org.example.financial_transaction.model.dto.CustomerUpdateRequest;
import org.example.financial_transaction.model.enumutation.AccountType;
import org.example.financial_transaction.model.enumutation.CustomerType;
import org.example.financial_transaction.service.IAccountService;
import org.example.financial_transaction.service.ICustomerService;
import org.example.financial_transaction.service.IHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository repository;
    private final IAccountService iAccountService;
    private final IHistoryService iHistoryService;

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
        Boolean isDuplicate = findDuplicateByNationalCodeAndId(customerUpdateRequest.nationalCode(), customerUpdateRequest.id());
        if (isDuplicate)
            throw new DuplicateException(customerUpdateRequest.nationalCode());
        Customer prevCustomer = findById(customerUpdateRequest.id());
        StringBuilder description = new StringBuilder();
        Optional.ofNullable(customerUpdateRequest.name()).ifPresent(newName -> {
            if (!newName.equals(prevCustomer.getName())) {
                description.append("name: ").append(newName).append(",");
                prevCustomer.setName(newName);
            }
        });
        Optional.ofNullable(customerUpdateRequest.nationalCode()).ifPresent(newNationalCode -> {
            if (!newNationalCode.equals(prevCustomer.getNationalCode())) {
                description.append("nationalCode: ").append(newNationalCode).append(",");
                prevCustomer.setNationalCode(newNationalCode);
            }
        });
        Optional.ofNullable(customerUpdateRequest.establishmentDate()).ifPresent(newDate -> {
            if (!newDate.equals(prevCustomer.getEstablishmentDate())) {
                description.append("establishmentDate: ").append(newDate).append(",");
                prevCustomer.setEstablishmentDate(newDate);
            }
        });
        Optional.ofNullable(customerUpdateRequest.customerType()).ifPresent(newType -> {
            if (!newType.equals(prevCustomer.getCustomerType())) {
                description.append("customerType: ").append(newType).append(",");
                prevCustomer.setCustomerType(newType);
            }
        });
        Optional.ofNullable(customerUpdateRequest.phoneNumber()).ifPresent(newPhoneNumber -> {
            if (!newPhoneNumber.equals(prevCustomer.getPhoneNumber())) {
                description.append("phoneNumber: ").append(newPhoneNumber).append(",");
                prevCustomer.setPhoneNumber(newPhoneNumber);
            }
        });
        Optional.ofNullable(customerUpdateRequest.address()).ifPresent(newAddress -> {
            if (!newAddress.equals(prevCustomer.getAddress())) {
                description.append("address: ").append(newAddress).append(",");
                prevCustomer.setAddress(newAddress);
            }
        });
        if (!description.isEmpty()) {
            description.append(" successfully updated");
            saveHistory(String.valueOf(description), prevCustomer);
        }
        repository.save(prevCustomer);
    }

    @Override
    public Customer findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public void saveHistory(String description, Customer customer) {
        String username = "username";
        History history = new History(username, LocalDateTime.now(), description, null, customer);
        iHistoryService.save(history);
    }

    @Override
    public Boolean findDuplicateByNationalCodeAndId(String nationalCode, Integer id) {
        return repository.findDuplicateByNationalCodeAndId(nationalCode, id);
    }

    @PostConstruct
    public void init() {
        if (!iAccountService.existsByAccountNumber("11111111111111")) {
            Account account = new Account("11111111111111", new Date(), AccountType.ACTIVE, 0D, null, null);
            Customer customer = new Customer("resalat bank", "1234567890", LocalDate.now(), "09124569874", "tehran bank resalat", "12345678", CustomerType.LEGAL, account);
            repository.save(customer);
        }

    }
}

