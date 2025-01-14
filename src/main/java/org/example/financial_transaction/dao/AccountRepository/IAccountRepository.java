package org.example.financial_transaction.dao.AccountRepository;

import org.example.financial_transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAccountRepository extends JpaRepository<Account, Integer> {
    @Query(value = """
            select seq_account_number.NEXTVAL from seq_account_number
            """, nativeQuery = true)
    Long generateAccountNumber();

    @Query("""
            select a.accountNumber from Account a left join a.customer c
             where c.nationalCode=:nationalCode
            """)
    String findAccountNumberByNationalCode(@Param("nationalCode") String nationalCode);

    @Query("""
            select a.balance from Account a where a.accountNumber =:accountNumber
            """)
    Double findBalanceByAccountNumber(@Param("accountNumber") String accountNumber);
}
