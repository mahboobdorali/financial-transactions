package org.example.financial_transaction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.financial_transaction.mapstruct.CustomerMapper;
import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.dto.CustomerRequest;
import org.example.financial_transaction.model.dto.CustomerUpdateRequest;
import org.example.financial_transaction.service.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerService customerService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@Valid @RequestBody CustomerRequest customerRequest) {
        Customer customer = CustomerMapper.INSTANCE.customerRequestToCustomer(customerRequest);
        customerService.registerCustomer(customer);
    }

    @PutMapping("/update")
    public void update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.update(customerUpdateRequest);
    }
}
