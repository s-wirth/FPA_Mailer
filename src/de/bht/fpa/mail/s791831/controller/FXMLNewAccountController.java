/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.controller;

import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Lena
 */
public class FXMLNewAccountController implements Initializable{

    @FXML
    private TextField nameText;
    @FXML
    private TextField hostText;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    
    private final FXMLMainViewController docController;
    
    FXMLNewAccountController(FXMLMainViewController contr) {
        docController = contr;
    }
    
    public void setEdit(Account acc){
        saveButton.setText("update");
        
        nameText.setText(acc.getName());
        hostText.setText(acc.getHost());
        usernameText.setText(acc.getUsername());
        passwordText.setText(acc.getPassword());
        
        saveButton.setOnAction((ActionEvent e) -> pressUpdate(acc));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cancelButton.setOnAction((ActionEvent e) -> pressCancel());
        saveButton.setOnAction((ActionEvent e) -> pressSave());     
    }
    
    /**
     * Closes window without creating a new account.
     */
    private void pressCancel() {
        Stage window = (Stage) cancelButton.getScene().getWindow();
        
        window.close();
    }

    /**
     * Saves new account (new name) if all text fields are filled in.
     */
    private void pressSave() {
        if (nameText.getText().isEmpty() || hostText.getText().isEmpty() || 
            usernameText.getText().isEmpty() || passwordText.getText().isEmpty()){
            errorLabel.setText("All textfields must contain data!");
        }else{
            Account acc = new Account(nameText.getText(), hostText.getText(), usernameText.getText(), passwordText.getText());
            
//            File accountDirectory = new File(new File("TestData"), acc.getName());
//            accountDirectory.mkdirs();
//            Folder folder = new Folder(accountDirectory, true);
//            acc.setTop(folder);
            
            if(docController.getAppLogic().saveAccount(acc)){
                docController.accountOpen.getItems().add(new MenuItem(acc.getName()));
                docController.configureMenu();
                Stage window = (Stage) saveButton.getScene().getWindow();
                window.close();
            }else{
                errorLabel.setText("Accout name already exists!");
            }  
        }
    }

    private void pressUpdate(Account acc) {
        if (nameText.getText().isEmpty() || hostText.getText().isEmpty() || 
            usernameText.getText().isEmpty() || passwordText.getText().isEmpty()){
            errorLabel.setText("All textfields must contain data!");
        }
        if(!acc.getName().equals(nameText.getText())){
            errorLabel.setText("Account name cannot be changed!");
            nameText.setText(acc.getName());
        }else{
            acc.setHost(hostText.getText());
            acc.setUsername(usernameText.getText());
            acc.setPassword(passwordText.getText());
            docController.getAppLogic().updateAccount(acc);
            Stage window = (Stage) saveButton.getScene().getWindow();
            window.close();
        }  
    }

    
    
}
