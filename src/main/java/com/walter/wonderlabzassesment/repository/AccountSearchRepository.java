package com.walter.wonderlabzassesment.repository;

import com.walter.wonderlabzassesment.model.BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountSearchRepository extends CrudRepository<BankAccount,Long> {

    @Query(value="SELECT * FROM BANK WHERE ACCOUNT_NUMBER = ? ",nativeQuery = true)
     BankAccount findByTech(String accountNum);

}