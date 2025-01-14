package org.example.financial_transaction.service;

import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.dto.CustomerUpdateRequest;

import java.util.Optional;

public interface ICustomerService {

    void registerCustomer(Customer customer);

    void update(CustomerUpdateRequest customerUpdateRequest);

    Customer findById(Integer id);
}
