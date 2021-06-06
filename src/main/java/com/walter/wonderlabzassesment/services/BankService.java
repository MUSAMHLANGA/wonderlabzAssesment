package com.walter.wonderlabzassesment.services;

import com.walter.wonderlabzassesment.model.BankAccount;
import com.walter.wonderlabzassesment.model.DepositWithdraw;
import com.walter.wonderlabzassesment.model.Transaction;
import com.walter.wonderlabzassesment.model.Transfer;
import com.walter.wonderlabzassesment.repository.AccountSearchRepository;
import com.walter.wonderlabzassesment.repository.BankRepository;
import com.walter.wonderlabzassesment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountSearchRepository accountSearchRepository;


    public static final double overDraftAmount = -100000;
    private static final double MIN_ACC_BALANCE = 1000;

    public static final String OPEN_ACCOUNT_TRANSACTION_DESCRIPTION = "Deposit Amount(new account)";
    public static final String DEPOSIT_TRANSACTION_DESCRIPTION = "Deposit Amount";
    public static final String TRANSFER_TRANSACTION_DESCRIPTION = "Transfer amount";
    public static final String WITHDRAW_TRANSACTION_DESCRIPTION = "Withdrawal amount";


    /*create an account
     */
    public BankAccount openBankAccount(String accountType,Double openBalance,String clientName){

        BankAccount newAccount = new BankAccount();
        String accountNumber = "";
        if(accountType.equalsIgnoreCase("Savings")){

             if(openBalance >=1000){

                 accountNumber = generateAccountNumber(accountType);

                 BankAccount bankAccount = new BankAccount(accountNumber,clientName,accountType,openBalance,0,openBalance);

                 newAccount =  bankRepository.save(bankAccount);

                //save the transaction
                 Transaction transaction = new Transaction();
                 transaction.setAccountBalance(openBalance);
                 transaction.setTransDescription(OPEN_ACCOUNT_TRANSACTION_DESCRIPTION);
                 transaction.setTransactionDate(getDate());
                 transaction.setTransAmount(openBalance);
                 transaction.setBankAccount(bankAccount);

                  List<Transaction> thelist = new ArrayList<>();
                  thelist.add(transaction);

                  transactionRepository.save(transaction);
             }

        }else if(accountType.equalsIgnoreCase("Current")){

            accountNumber = generateAccountNumber(accountType);
            Transaction transaction = new Transaction();

            BankAccount bankAccount = new BankAccount(accountNumber,clientName,accountType,openBalance,overDraftAmount,openBalance);
            System.out.println(accountNumber);
            newAccount =  bankRepository.save(bankAccount);

            //save the transaction
            transaction.setAccountBalance(openBalance);
            transaction.setTransDescription(OPEN_ACCOUNT_TRANSACTION_DESCRIPTION);
            transaction.setTransactionDate(getDate());
            transaction.setTransAmount(openBalance);
            transaction.setBankAccount(bankAccount);
             List<Transaction> thelist = new ArrayList<>();
             thelist.add(transaction);

            transactionRepository.save(transaction);

        }
        return newAccount;


    }
    /*
        method to deposit money
     */
    public BankAccount depositAmount(DepositWithdraw depositWithdraw){

        Transaction transaction = new Transaction();
        List<Transaction> theList = new ArrayList<>();
        BankAccount bankAccount = new BankAccount();

        if(depositWithdraw.getAccountNumber() != null && depositWithdraw.getTransAmount() >0){

            if(accountSearchRepository.findByTech(depositWithdraw.getAccountNumber()) != null){

               BankAccount bankAccount1 =  accountSearchRepository.findByTech(depositWithdraw.getAccountNumber());

                if(bankAccount1.getOverDraftAmount() > -100000 && bankAccount1.getAccountBalance() < 0 ){
                    double overDraft = bankAccount1.getOverDraftAmount() + depositWithdraw.getTransAmount();
                    bankAccount1.setOverDraftAmount(overDraft);

                }else{

                    bankAccount1.setAccountBalance(deposit(bankAccount1.getAccountBalance(),depositWithdraw.getTransAmount()));
                }

                bankAccount1.setOpeningBalance(0);

                bankAccount = bankRepository.save(bankAccount1);

                // save the transaction
                transaction.setAccountBalance(bankAccount1.getAccountBalance());

                transaction.setTransDescription(DEPOSIT_TRANSACTION_DESCRIPTION);
                transaction.setTransactionDate(getDate());
                transaction.setTransAmount(depositWithdraw.getTransAmount());
                transaction.setBankAccount(bankAccount1);

                theList.add(transaction);

                transactionRepository.save(transaction);

                 return bankAccount;
            }
        }
        return bankAccount;
    }

/*
    method to transfer amoney to other existing account
 */
    public BankAccount transferMoney(Transfer transfer){
        BankAccount response = new BankAccount();

        List<Transaction> theList = new ArrayList<>();
        if(transfer.getFromAccount() != null && transfer.getAccountTo() != null){
            if(accountSearchRepository.findByTech(transfer.getFromAccount())!= null){
                if(accountSearchRepository.findByTech(transfer.getFromAccount())!= null){

                    BankAccount accoutFrom = new BankAccount();

                    accoutFrom = accountSearchRepository.findByTech(transfer.getFromAccount());

                    if(accoutFrom != null && accoutFrom.getAccountBalance() > transfer.getTransAmount() ){

                        double newBalance = accoutFrom.getAccountBalance()- transfer.getTransAmount();
                        accoutFrom.setAccountBalance(newBalance);
                        accoutFrom.setOpeningBalance(0);
                        bankRepository.save(accoutFrom);

                        // save the transaction
                        Transaction transaction = new Transaction();
                        transaction.setBankAccount(accoutFrom);
                        transaction.setTransDescription(TRANSFER_TRANSACTION_DESCRIPTION);
                        transaction.setTransactionDate(getDate());
                        transaction.setTransAmount(transfer.getTransAmount());
                        transaction.setAccountBalance(newBalance);
                        theList.add(transaction);
                        transactionRepository.save(transaction);


                        BankAccount accountTo = new BankAccount();
                        accountTo = accountSearchRepository.findByTech(transfer.getAccountTo());
                        newBalance = accountTo.getAccountBalance() + transfer.getTransAmount();
                        accountTo.setAccountBalance(newBalance);
                        accountTo.setOpeningBalance(0);
                        response = bankRepository.save(accountTo);

                        // save the transaction
                        Transaction _transaction = new Transaction();
                        _transaction.setBankAccount(accountTo);
                        _transaction.setTransDescription(TRANSFER_TRANSACTION_DESCRIPTION);
                        _transaction.setTransactionDate(getDate());
                        _transaction.setTransAmount(transfer.getTransAmount());
                        _transaction.setAccountBalance(newBalance);
                        theList.add(transaction);
                        transactionRepository.save(_transaction);

                        return  response;
                    }
                }
            }

        }
        return  response;
    }

    public BankAccount withdrawAmount(DepositWithdraw depositWithdraw){

        BankAccount newBankAccount = new BankAccount();

        if(depositWithdraw.getTransAmount() > 0){

            BankAccount oldBankAccount = new BankAccount();
            oldBankAccount = accountSearchRepository.findByTech(depositWithdraw.getAccountNumber());

            if(oldBankAccount.getAccountType().equalsIgnoreCase("Savings")){


                double  avalableWithdrawal = oldBankAccount.getAccountBalance() - depositWithdraw.getTransAmount();

                if(avalableWithdrawal >= MIN_ACC_BALANCE){

                    double newBalance  = avalableWithdrawal - depositWithdraw.getTransAmount();
                    oldBankAccount.setAccountBalance(newBalance);
                    oldBankAccount.setOpeningBalance(0);
                    newBankAccount = bankRepository.save(oldBankAccount);

                    //save the withdrawal transaction
                    Transaction _transaction = new Transaction();
                    List<Transaction> theList = new ArrayList<>();
                    _transaction.setBankAccount(oldBankAccount);
                    _transaction.setTransDescription(WITHDRAW_TRANSACTION_DESCRIPTION);
                    _transaction.setTransactionDate(getDate());
                    _transaction.setTransAmount(depositWithdraw.getTransAmount());
                    _transaction.setAccountBalance(newBalance);
                    theList.add(_transaction);
                    transactionRepository.save(_transaction);
                }


            }else if(oldBankAccount.getAccountType().equalsIgnoreCase("Current")){

                if(oldBankAccount.getAccountBalance() < depositWithdraw.getTransAmount()){

                    double neededOverdraft = oldBankAccount.getAccountBalance() - depositWithdraw.getTransAmount();


                        double overDraftBalance = oldBankAccount.getOverDraftAmount() + Math.abs(neededOverdraft);

                        double totWithdrawal = Math.abs(neededOverdraft) + depositWithdraw.getTransAmount();

                        oldBankAccount.setOverDraftAmount(overDraftBalance);
                        oldBankAccount.setAccountBalance(neededOverdraft);
                        oldBankAccount.setOpeningBalance(0);
                        newBankAccount = bankRepository.save(oldBankAccount);

                        // save the withdrawal transaction
                        Transaction _transaction = new Transaction();
                        List<Transaction> theList = new ArrayList<>();
                        _transaction.setBankAccount(oldBankAccount);
                        _transaction.setTransDescription(WITHDRAW_TRANSACTION_DESCRIPTION);
                        _transaction.setTransactionDate(getDate());
                        _transaction.setTransAmount(totWithdrawal);
                        _transaction.setAccountBalance(neededOverdraft);
                        theList.add(_transaction);
                        transactionRepository.save(_transaction);

                }else{

                    double newBalance = oldBankAccount.getAccountBalance() - depositWithdraw.getTransAmount();
                    oldBankAccount.setAccountBalance(newBalance);
                    oldBankAccount.setOpeningBalance(0);
                    newBankAccount =  bankRepository.save(oldBankAccount);

                    //save the withdrawal transaction
                    Transaction _transaction = new Transaction();
                    List<Transaction> theList = new ArrayList<>();
                    _transaction.setBankAccount(oldBankAccount);
                    _transaction.setTransDescription(WITHDRAW_TRANSACTION_DESCRIPTION);
                    _transaction.setTransactionDate(getDate());
                    _transaction.setTransAmount(depositWithdraw.getTransAmount());
                    _transaction.setAccountBalance(newBalance);
                    theList.add(_transaction);
                    transactionRepository.save(_transaction);
                }

            }
        }

        return newBankAccount;
    }

    public List<Transaction>  getAllAccountTransaction(String accountNumber){

        List<Transaction> theList = new ArrayList<>();
        BankAccount bankAccount = new BankAccount();
        bankAccount = accountSearchRepository.findByTech(accountNumber);
        if(bankAccount != null){
            BankAccount _bankAccount = new BankAccount();
            _bankAccount = bankRepository.findById(bankAccount.getAccountID()).get();
            if(_bankAccount != null){
                theList = _bankAccount.getTheList();
                System.out.println("The account"+_bankAccount.getAccountNumber()+" has "+theList.size()+" number of transactions");
            }
        }

        return  theList;
    }
     //current account number start with 44 and savings start with 99
     // generating the account number with the use of current date and first 5 random numbers 1=>5

    public String generateAccountNumber(String accountType){

        String accountNumber = "";

        LocalDate localDate = LocalDate.now();
        String date = localDate.toString().replace("-","");

        // define the range
        int max = 5;
        int min = 1;
        int range = max - min + 1;
        String results = "";
        // generate random numbers within 1 to 10
        for (int i = 0; i < 5; i++) {
            int rand = (int)(Math.random() * range) + min;
            results+=rand;
        }

        if(accountType.equalsIgnoreCase("Savings")){

            accountNumber = "99"+date+results;

        }else if(accountType.equalsIgnoreCase("Current")){

            accountNumber = "44"+date+results;
        }

        return  accountNumber;
    }

    public String getDate(){
        LocalDate localDate = LocalDate.now();

        return  localDate.toString();
    }

    public  double deposit(double amount,double balance){

       double totalBalance = amount+balance;
        return totalBalance;
    }

}
