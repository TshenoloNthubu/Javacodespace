package com.banking;

import com.banking.controller.BankController;
import com.banking.dao.DatabaseConnection;
import com.banking.model.Bank;
import com.banking.model.Customer;
import com.banking.model.Account;
import java.util.Scanner;

public class Main {

    private static BankController bankController;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Banking System Starting ===");

        // Initialize database and core components
        DatabaseConnection.initializeDatabase();
        Bank bank = new Bank("Botswana Banking System");
        bankController = new BankController(bank);

        // Load sample data for testing
        loadSampleData();

        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createCustomer();
                    break;
                case "2":
                    openAccount();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    withdraw();
                    break;
                case "5":
                    checkBalance();
                    break;
                case "6":
                    listCustomers();
                    break;
                case "7":
                    applyInterest();
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== BANKING SYSTEM ===");
        System.out.println("1. Create Customer");
        System.out.println("2. Open Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Check Balance");
        System.out.println("6. List Customers & Accounts");
        System.out.println("7. Apply Monthly Interest");
        System.out.println("0. Exit");
    }

    private static void loadSampleData() {
        // Add sample customers and accounts for testing
        bankController.addCustomer("C001", "John", "Smith", "123 Main St, Gaborone");
        bankController.addCustomer("C002", "Mary", "Johnson", "456 Broad St, Francistown");
        bankController.addCustomer("C003", "David", "Brown", "789 River Rd, Maun");

        bankController.openSavingsAccount("SAV001", 1000.00, "Main Branch", "C001");
        bankController.openInvestmentAccount("INV001", 1500.00, "Main Branch", "C001");
        bankController.openChequeAccount("CHQ001", 2000.00, "City Branch", "C002", "Tech Corp", "Gaborone");

        System.out.println("✓ Sample data loaded successfully");
    }

    private static void createCustomer() {
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        boolean success = bankController.addCustomer(customerId, firstName, surname, address);
        if (success) {
            System.out.println("✓ Customer created successfully!");
        } else {
            System.out.println("❌ Failed to create customer. Customer ID might already exist.");
        }
    }

    private static void openAccount() {
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();

        System.out.println("Select Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Investment Account (Min: BWP 500)");
        System.out.println("3. Cheque Account");
        System.out.print("Enter choice: ");
        String accountTypeChoice = scanner.nextLine();

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter Initial Deposit: ");
        double initialDeposit = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter Branch: ");
        String branch = scanner.nextLine();

        boolean success = false;

        switch (accountTypeChoice) {
            case "1":
                success = bankController.openSavingsAccount(accountNumber, initialDeposit, branch, customerId);
                break;
            case "2":
                success = bankController.openInvestmentAccount(accountNumber, initialDeposit, branch, customerId);
                break;
            case "3":
                System.out.print("Enter Employer Name: ");
                String employerName = scanner.nextLine();
                System.out.print("Enter Company Address: ");
                String companyAddress = scanner.nextLine();
                success = bankController.openChequeAccount(accountNumber, initialDeposit, branch, customerId, employerName, companyAddress);
                break;
            default:
                System.out.println("Invalid account type.");
                return;
        }

        if (success) {
            System.out.println("✓ Account opened successfully!");
        } else {
            System.out.println("❌ Failed to open account. Check customer ID and requirements.");
        }
    }

    private static void deposit() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean success = bankController.deposit(accountNumber, amount);
        if (success) {
            System.out.println("✓ Deposit completed successfully!");
        } else {
            System.out.println("❌ Deposit failed. Check account number.");
        }
    }

    private static void withdraw() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        System.out.print("Enter Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean success = bankController.withdraw(accountNumber, amount);
        if (success) {
            System.out.println("✓ Withdrawal completed successfully!");
        } else {
            System.out.println("❌ Withdrawal failed. Check account number and balance.");
        }
    }

    private static void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        bankController.getBalance(accountNumber).ifPresentOrElse(
                balance -> System.out.println("Account Balance: BWP " + balance),
                () -> System.out.println("❌ Account not found.")
        );
    }

    private static void listCustomers() {
        System.out.println("\n=== CUSTOMERS ===");
        var customers = bankController.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println("\nCustomer: " + customer.getFirstName() + " " + customer.getSurname());
            System.out.println("ID: " + customer.getCustomerId() + " | Address: " + customer.getAddress());

            var accounts = bankController.getCustomerAccounts(customer.getCustomerId());
            if (accounts.isEmpty()) {
                System.out.println("  No accounts");
            } else {
                System.out.println("  Accounts:");
                for (Account account : accounts) {
                    System.out.printf("  - %s (%s): BWP %.2f | Branch: %s%n",
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getBalance(),
                            account.getBranch());
                }
            }
        }
    }

    private static void applyInterest() {
        bankController.applyMonthlyInterest();
        System.out.println("✓ Monthly interest applied to all accounts!");
    }
}