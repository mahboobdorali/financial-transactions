package org.example.financial_transaction.dao;

import org.example.financial_transaction.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByNationalCode(String nationalCode);
}
