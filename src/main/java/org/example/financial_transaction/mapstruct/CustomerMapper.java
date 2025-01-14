package org.example.financial_transaction.mapstruct;

import org.example.financial_transaction.model.Customer;
import org.example.financial_transaction.model.dto.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer customerRequestToCustomer(CustomerRequest customerRequest);
}
