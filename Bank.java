package com.banking.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bank {
    private List<Customer> customers;
    private List<Account> accounts;
    private String bankName;

    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    // Customer management
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Optional<Customer> findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst();
    }

    // Account management
    public void addAccount(Account account) {
        accounts.add(account);
        account.getCustomer().addAccount(account);
    }

    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public List<Account> getCustomerAccounts(String customerId) {
        return findCustomerById(customerId)
                .map(Customer::getAccounts)
                .orElse(new ArrayList<>());
    }

    // Transaction methods
    public boolean deposit(String accountNumber, double amount) {
        Optional<Account> account = findAccountByNumber(accountNumber);
        if (account.isPresent() && amount > 0) {
            account.get().deposit(amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Optional<Account> account = findAccountByNumber(accountNumber);
        return account.map(acc -> acc.withdraw(amount)).orElse(false);
    }

    // Apply monthly interest to all interest-bearing accounts
    public void applyMonthlyInterest() {
        for (Account account : accounts) {
            account.applyMonthlyInterest();
        }
    }

    // Getters
    public List<Customer> getCustomers() { return new ArrayList<>(customers); }
    public List<Account> getAccounts() { return new ArrayList<>(accounts); }
    public String getBankName() { return bankName; }
}