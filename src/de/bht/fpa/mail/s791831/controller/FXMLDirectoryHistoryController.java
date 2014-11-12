/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.controller;

import de.bht.fpa.mail.s791831.model.appLogic.FileManager;
import de.bht.fpa.mail.s791831.model.appLogic.FolderManagerIF;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller for directory history.
 *
 * @author Lena
 */
public class FXMLDirectoryHistoryController implements Initializable {

    @FXML
    private ListView<File> list;
    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    private final ObservableList INPUT;
    private final FXMLMainViewController docController;
    
    /**
     * Creates a new directory history controller.
     * @param contr controller from main view
     * @param oList ObservableList contains all previous chosen directories
     */
    FXMLDirectoryHistoryController(FXMLMainViewController contr, ObservableList oList) {
        docController = contr;
        INPUT = oList;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadInput();
        cancelButton.setOnAction((ActionEvent e) -> pressCancel());
        okButton.setOnAction((ActionEvent e) -> pressOK());
    } 
    
    /**
     * Lists previous chosen directories.
     * If there are no previous chosen directories load empty input message and disable ok button.
     */
    public void loadInput(){
        if(INPUT.isEmpty()){
            ObservableList emptyInput = FXCollections.observableArrayList();
            emptyInput.add("No base directories in history");
            list.setItems(emptyInput);
            okButton.setDisable(true);
            return;
        }
        list.setItems(INPUT);
    }

    /**
     * Closes window without selecting an item from the list.
     */
    private void pressCancel() {
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.close();
    }

    /**
     * Selects an item from the list and configures Treeview.
     * Closes window after clicking.
     */
    private void pressOK() {
        File selectedFile = list.getSelectionModel().getSelectedItem();
        FolderManagerIF fm = new FileManager(selectedFile);
        docController.configureTree(fm);
        Stage window = (Stage) okButton.getScene().getWindow();
        window.close();
    }
    
}
