package com.banking;

import com.banking.controller.BankController;
import com.banking.model.Bank;
import com.banking.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {

    private static BankController bankController;

    public static BankController getBankController() {
        return bankController;
    }

    @Override
    public void start(Stage stage) {
        // Initialize bank system for GUI mode
        Bank bank = new Bank("Botswana Banking System");
        bankController = new BankController(bank);

        // Launch your JavaFX UI
        new MainView().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

