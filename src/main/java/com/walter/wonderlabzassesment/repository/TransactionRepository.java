package com.walter.wonderlabzassesment.repository;

import com.walter.wonderlabzassesment.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
}
