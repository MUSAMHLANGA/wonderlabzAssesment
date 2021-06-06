package com.walter.wonderlabzassesment.repository;

import com.walter.wonderlabzassesment.model.BankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends CrudRepository<BankAccount,Long> {



}
