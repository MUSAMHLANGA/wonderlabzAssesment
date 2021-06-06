package com.walter.wonderlabzassesment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name="TRANSACTION")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long transID;
    private String transactionDate;
    private String transDescription;
    private double  transAmount;
    private double accountBalance;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="accountID", nullable=false)
    private BankAccount bankAccount;

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Transaction() {
    }

    public Transaction(String transactionDate, String transDescription, double transAmount, double accountBalance) {
        this.transactionDate = transactionDate;
        this.transDescription = transDescription;
        this.transAmount = transAmount;
        this.accountBalance = accountBalance;

    }

    public Long getTransID() {
        return transID;
    }

    public void setTransID(long transID) {
        this.transID = transID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

}
