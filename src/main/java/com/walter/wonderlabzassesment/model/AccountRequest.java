package com.walter.wonderlabzassesment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class AccountRequest implements Serializable {

    private String accountType;
    private Double openingBalance;
    private String clientName;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
