package com.walter.wonderlabzassesment.model;

import java.io.Serializable;

public class DepositWithdraw implements Serializable {
    private String accountNumber;
    private double transAmount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }
}
