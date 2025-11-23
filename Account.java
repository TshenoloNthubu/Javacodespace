package com.banking.model;

public abstract class Account {

    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    protected AccountType accountType;

    public Account(String accountNumber, double balance, String branch,
                   Customer customer, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.accountType = accountType;
    }

    public abstract boolean withdraw(double amount);
    public abstract void applyMonthlyInterest();

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + " New Balance: " + balance);
        }
    }

    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public Customer getCustomer() { return customer; }
    public AccountType getAccountType() { return accountType; }
    public String getBranch() { return branch; }

    @Override
    public String toString() {
        return accountType + " Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}
