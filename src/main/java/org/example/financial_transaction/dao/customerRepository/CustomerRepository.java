 package org.example.financial_transaction.dao.customerRepository;

 import jakarta.validation.constraints.NotNull;
 import org.example.financial_transaction.model.Customer;
 import org.springframework.data.jpa.repository.JpaRepository;

 import java.util.Optional;

 public interface CustomerRepository extends JpaRepository<Customer, Integer> {

     Optional<Customer> findByNationalCode(@NotNull String nationalCode);
 }
