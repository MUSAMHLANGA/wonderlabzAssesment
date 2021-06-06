package com.walter.wonderlabzassesment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name="BANK")
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long accountID;
    private String accountNumber;
    private String clientName;
    private String accountType;
    private double openingBalance;
    private double overDraftAmount;
    private double accountBalance;
    @JsonIgnore
    @OneToMany(mappedBy = "bankAccount",cascade=CascadeType.ALL)
    private List<Transaction> theList;

    public List<Transaction> getTheList() {
        return theList;
    }

    public void setTheList(List<Transaction> theList) {
        this.theList = theList;
    }

    public BankAccount() {
    }

    public BankAccount(String accountNumber, String clientName, String accountType, double openingBalance, double overDraftAmount, double accountBalance) {
        this.accountNumber = accountNumber;
        this.clientName = clientName;
        this.accountType = accountType;
        this.openingBalance = openingBalance;
        this.overDraftAmount = overDraftAmount;
        this.accountBalance = accountBalance;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getOverDraftAmount() {
        return overDraftAmount;
    }

    public void setOverDraftAmount(double overDraftAmount) {
        this.overDraftAmount = overDraftAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
