package com.banking.model;

public class SavingsAccount extends Account {

    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer, AccountType.SAVINGS);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals not allowed for Savings Account");
        return false;
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("Applied monthly interest: " + interest);
    }
}
