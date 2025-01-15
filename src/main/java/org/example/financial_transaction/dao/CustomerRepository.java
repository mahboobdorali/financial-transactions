package org.example.financial_transaction.dao;

import jakarta.validation.constraints.NotNull;
import org.example.financial_transaction.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByNationalCode(@NotNull String nationalCode);

    @Query("""
            select count(c) > 0 from Customer c where c.nationalCode = :nationalCode and c.id<>:id
            """)
    Boolean findDuplicateByNationalCode(@Param("nationalCode") String nationalCode, @Param("id") Integer id);
}
