package com.banking.model;

public class ChequeAccount extends Account {

    private String employerName;
    private String companyAddress;

    public ChequeAccount(String accountNumber, double balance, String branch, Customer customer,
                         String employerName, String companyAddress) {
        super(accountNumber, balance, branch, customer, AccountType.CHEQUE);
        this.employerName = employerName;
        this.companyAddress = companyAddress;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
            return true;
        }
        System.out.println("Insufficient funds");
        return false;
    }

    @Override
    public void applyMonthlyInterest() {
        System.out.println("No interest on Cheque Account");
    }

    public String getEmployerName() { return employerName; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public String toString() {
        return super.toString() + " Employer: " + employerName;
    }
}
