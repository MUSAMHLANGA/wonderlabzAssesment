package com.walter.wonderlabzassesment.rest;

import com.walter.wonderlabzassesment.model.*;
import com.walter.wonderlabzassesment.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class BankController {

    protected BankService bankService;


    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping(value = "/openAccount")
    public  BankAccount openAccount(@RequestBody AccountRequest accountRequest){

        BankAccount newAccount = new BankAccount();

        newAccount =  bankService.openBankAccount(accountRequest.getAccountType(),accountRequest.getOpeningBalance(),accountRequest.getClientName());

        return  newAccount;
    }

    @PutMapping(value = "/deposit")
    public @ResponseBody BankAccount depositAmount(@RequestBody DepositWithdraw dw){

        BankAccount bk = new BankAccount();

        bk =  bankService.depositAmount(dw);
        return  bk;
    }

    @PutMapping("/transfer")
    public BankAccount transferAmmount(@RequestBody Transfer transfer){

       BankAccount response = new BankAccount();

        if(transfer.getTransAmount() >=0  && transfer.getAccountTo() != null && transfer.getFromAccount() != null){

            response = bankService.transferMoney(transfer);
        }
        return response;
    }

    @PutMapping("/withdraw")
    public BankAccount withdrawAmount(@RequestBody DepositWithdraw depositWithdraw){

        BankAccount newBank = new BankAccount();
        if(depositWithdraw != null){

            newBank = bankService.withdrawAmount(depositWithdraw);
        }
        return newBank;
    }

    @GetMapping("/transactions/{account}")
    public List<Transaction> viewTransactionHistory(@PathVariable("account") String accountNumber){

        List<Transaction> theList = new ArrayList<>();

        theList = bankService.getAllAccountTransaction(accountNumber);

        return theList;
    }

}
