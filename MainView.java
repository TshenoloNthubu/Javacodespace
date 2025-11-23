package com.banking.view;

import com.banking.Main;
import com.banking.controller.BankController;
import com.banking.model.Customer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView extends Application {

    private BankController bankController;
    private ObservableList<Customer> customers;

    @Override
    public void start(Stage primaryStage) {
        // FIXED: Use Main.getBankController() instead of MainFX
        bankController = MainFX.getBankController();

        // Load customers into table
        customers = FXCollections.observableArrayList(bankController.getAllCustomers());

        initializeUI(primaryStage);
    }

    private void initializeUI(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createMenuBar());
        root.setCenter(createTabPane());

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setTitle("Botswana Banking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem refreshItem = new MenuItem("Refresh Data");
        MenuItem exitItem = new MenuItem("Exit");

        refreshItem.setOnAction(e -> refreshData());
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(refreshItem, new SeparatorMenuItem(), exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private void refreshData() {
        customers.setAll(bankController.getAllCustomers());
        showAlert("Data Refreshed", "Customer data has been refreshed.");
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();

        Tab customerTab = new Tab("Customer Management");
        customerTab.setClosable(false);
        customerTab.setContent(createCustomerManagementPanel());

        Tab accountTab = new Tab("Account Management");
        accountTab.setClosable(false);
        accountTab.setContent(createAccountManagementPanel());

        Tab transactionTab = new Tab("Transactions");
        transactionTab.setClosable(false);
        transactionTab.setContent(createTransactionPanel());

        tabPane.getTabs().addAll(customerTab, accountTab, transactionTab);
        return tabPane;
    }

    private VBox createCustomerManagementPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        Label title = new Label("Customer Management");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        TextField idField = new TextField();
        TextField firstField = new TextField();
        TextField surnameField = new TextField();
        TextField addressField = new TextField();

        formGrid.add(new Label("Customer ID:"), 0, 0);
        formGrid.add(idField, 1, 0);
        formGrid.add(new Label("First Name:"), 0, 1);
        formGrid.add(firstField, 1, 1);
        formGrid.add(new Label("Surname:"), 0, 2);
        formGrid.add(surnameField, 1, 2);
        formGrid.add(new Label("Address:"), 0, 3);
        formGrid.add(addressField, 1, 3);

        Button addBtn = new Button("Add Customer");
        Button clearBtn = new Button("Clear");

        addBtn.setOnAction(e -> {
            boolean ok = bankController.addCustomer(
                    idField.getText(),
                    firstField.getText(),
                    surnameField.getText(),
                    addressField.getText()
            );

            if (ok) {
                showAlert("Success", "Customer added!");
                refreshData();
                idField.clear();
                firstField.clear();
                surnameField.clear();
                addressField.clear();
            } else {
                showAlert("Error", "Failed to add customer (duplicate ID?).");
            }
        });

        clearBtn.setOnAction(e -> {
            idField.clear();
            firstField.clear();
            surnameField.clear();
            addressField.clear();
        });

        HBox buttonBox = new HBox(10, addBtn, clearBtn);

        TableView<Customer> table = new TableView<>(customers);

        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCustomerId()));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getFirstName() + " " + c.getValue().getSurname()));

        TableColumn<Customer, String> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));

        table.getColumns().addAll(idCol, nameCol, addrCol);

        panel.getChildren().addAll(title, formGrid, buttonBox, new Label("Customers:"), table);

        return panel;
    }

    private VBox createAccountManagementPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        Label title = new Label("Account Management");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();
        RadioButton savings = new RadioButton("Savings");
        RadioButton investment = new RadioButton("Investment");
        RadioButton cheque = new RadioButton("Cheque");

        savings.setToggleGroup(group);
        investment.setToggleGroup(group);
        cheque.setToggleGroup(group);

        HBox typeBox = new HBox(10, savings, investment, cheque);

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);

        TextField custField = new TextField();
        TextField accNumField = new TextField();
        TextField depositField = new TextField();
        TextField branchField = new TextField();
        TextField employerField = new TextField();
        TextField companyField = new TextField();

        form.add(new Label("Customer ID:"), 0, 0); form.add(custField, 1, 0);
        form.add(new Label("Account Number:"), 0, 1); form.add(accNumField, 1, 1);
        form.add(new Label("Initial Deposit:"), 0, 2); form.add(depositField, 1, 2);
        form.add(new Label("Branch:"), 0, 3); form.add(branchField, 1, 3);
        form.add(new Label("Employer (Cheque):"), 0, 4); form.add(employerField, 1, 4);
        form.add(new Label("Company Address (Cheque):"), 0, 5); form.add(companyField, 1, 5);

        Button openBtn = new Button("Open Account");
        openBtn.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected == null) {
                showAlert("Error", "Select account type!");
                return;
            }

            String type = selected.getText();
            double deposit;

            try {
                deposit = Double.parseDouble(depositField.getText());
            } catch (Exception ex) {
                showAlert("Error", "Invalid deposit amount.");
                return;
            }

            boolean ok = false;

            switch (type) {
                case "Savings":
                    ok = bankController.openSavingsAccount(
                            accNumField.getText(), deposit, branchField.getText(), custField.getText());
                    break;

                case "Investment":
                    ok = bankController.openInvestmentAccount(
                            accNumField.getText(), deposit, branchField.getText(), custField.getText());
                    break;

                case "Cheque":
                    ok = bankController.openChequeAccount(
                            accNumField.getText(), deposit, branchField.getText(), custField.getText(),
                            employerField.getText(), companyField.getText());
                    break;
            }

            if (ok) showAlert("Success", type + " account opened.");
            else showAlert("Error", "Failed to open account.");
        });

        panel.getChildren().addAll(title, typeBox, form, openBtn);
        return panel;
    }

    private VBox createTransactionPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        Label title = new Label("Transactions");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10);

        TextField accField = new TextField();
        TextField amountField = new TextField();
        TextArea history = new TextArea();
        history.setPrefHeight(200);

        form.add(new Label("Account Number:"), 0, 0); form.add(accField, 1, 0);
        form.add(new Label("Amount:"), 0, 1); form.add(amountField, 1, 1);

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button balanceBtn = new Button("Check Balance");

        depositBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (bankController.deposit(accField.getText(), amt)) {
                    history.appendText("Deposited BWP " + amt + "\n");
                } else showAlert("Error", "Deposit failed.");
            } catch (Exception ex) {
                showAlert("Error", "Invalid amount.");
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText());
                if (bankController.withdraw(accField.getText(), amt)) {
                    history.appendText("Withdrew BWP " + amt + "\n");
                } else showAlert("Error", "Withdrawal failed.");
            } catch (Exception ex) {
                showAlert("Error", "Invalid amount.");
            }
        });

        balanceBtn.setOnAction(e -> {
            bankController.getBalance(accField.getText())
                    .ifPresentOrElse(
                            bal -> {
                                history.appendText("Balance: BWP " + bal + "\n");
                                showAlert("Balance", "BWP " + bal);
                            },
                            () -> showAlert("Error", "Account not found.")
                    );
        });

        HBox buttons = new HBox(10, depositBtn, withdrawBtn, balanceBtn);

        panel.getChildren().addAll(title, form, buttons, new Label("Transaction History:"), history);
        return panel;
    }

    private void showAlert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
