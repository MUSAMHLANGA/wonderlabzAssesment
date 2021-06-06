package com.walter.wonderlabzassesment.repository;

import com.walter.wonderlabzassesment.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSearchRepository extends CrudRepository<BankAccount,Long> {

    @Query(value="SELECT * FROM BANK WHERE ACCOUNT_NUMBER = ? ",nativeQuery = true)
    public BankAccount findByTech(String accountNum);

    @Query(value="UPDATE BANK SET ACCOUNT_BALANCE = accountBalance WHERE ACCOUNT_NUMBER = accountNum ",nativeQuery = true)
    public BankAccount updateAccount(BankAccount bankAccount);
}
