/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.controller;

import de.bht.fpa.mail.s791831.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s791831.model.appLogic.FileManager;
import de.bht.fpa.mail.s791831.model.appLogic.FolderManagerIF;
import de.bht.fpa.mail.s791831.model.appLogic.XmlEmailManager;
import de.bht.fpa.mail.s791831.model.data.Component;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class is the controller of the FPA-Mailer.
 * Used FXML document: FXMLMainView.fxml
 * Initializes all FPA-Mailer functions. 
 * @author Lena
 */
public class FXMLMainViewController implements Initializable {
    
    @FXML
    private TreeView<Component> treeView;
    @FXML
    private Menu menuFile;
    @FXML
    private MenuItem fileOpen;
    @FXML
    private MenuItem fileHistory;
    
    /**
     * used icons for folder/files
     */
    private final Image FOLDER_ICON = new Image(getClass().getResourceAsStream("blue-folder.png"));
    private final Image FOLDER_OPEN_ICON = new Image(getClass().getResourceAsStream("blue-folder-open.png"));
    private final Image DOCUMENT_ICON = new Image(getClass().getResourceAsStream("blue-document.png"));
    
    /**
     * home directory
     */
    private final File HOME = new File(System.getProperty("user.home"));
    
    /**
     * filemanager for folder model
     */
    private final FolderManagerIF FILE_MANAGER = new FileManager(HOME);
    
    /**
     * emailmanager for email model
     */
    private final EmailManagerIF EMAIL_MANAGER = new XmlEmailManager();
   
    /**
     * saves all directories chosen in history
     */
    private final ObservableList INPUT = FXCollections.observableArrayList();
    
    /**
     * Intitializes Treeview and Menu.
     * @param url application URL
     * @param rb bundle for handling language translations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree(FILE_MANAGER);
        configureMenu();
    } 
    
    /**
     * Adds an event to all MenuItems.
     */
    public void configureMenu(){
        for(MenuItem m: menuFile.getItems()){
            m.setOnAction((e) -> menuEvent(e));
        }
//        fileOpen.setOnAction((ActionEvent e) -> directoryChooserEvent());
//        fileHistory.setOnAction((ActionEvent e) -> historyEvent());
    }
    
    /**
     * Creates a TreeView with root user.home.
     * Adds events for expanding, collapsing.
     * Adds a Listener to select items.
     * @param fileManager filemanager for folder model with current root folder
     */
    public void configureTree(FolderManagerIF fileManager){
        
        /*load content of current root folder from filemanager and set as root of TreeView*/
        fileManager.loadContent(fileManager.getTopFolder());
        TreeItem<Component> topFolder = new TreeItem<>(fileManager.getTopFolder(),new ImageView(FOLDER_OPEN_ICON));
        showFolder(topFolder);
        topFolder.setExpanded(true);
        treeView.setRoot(topFolder);
        
        /*add EventHandler to root - tranfered to all TreeItems*/
        topFolder.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> expandEvent(e, fileManager));
        topFolder.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<Component> e) -> collapseEvent(e));
        
        /*add Listener to TreeView to get info about selected TreeItem*/
        treeView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Component>> observable, 
                TreeItem<Component> oldValue, TreeItem<Component> newValue) -> showEmail(newValue));
    }
    
    /**
     * Searches for emails in the content of the selected TreeItem.
     * Prints on console:
     *      - path of the selcted folder
     *      - number of emails in selected folder
     *      - sender, receiving date and subject of each email
     * @param item selected TreeItem
     */
    private void showEmail(TreeItem<Component> item){
        System.out.println("Selected directory: " + item.getValue().getPath());
        
        Folder folder = (Folder) item.getValue();
        EMAIL_MANAGER.loadContent(folder);
        System.out.println("Number of emails: " + folder.getEmails().size());
        
        List<Email> list = folder.getEmails();
        for(Email email : list){
            System.out.println("[Email: sender=" + email.getSender() + " received=" + email.getReceived() + " subject=" + email.getSubject() + "]");
        }
    }
    
    /**
     * Decides which event schould be triggert by selected MenuItem. 
     * @param e Event to get clicked MenuItem
     */
    private void menuEvent(Event e) {
        if(e.getSource() == fileOpen){
            directoryChooserEvent();
        }
        if(e.getSource() == fileHistory){
            historyEvent();
        }
    }
    
    /**
     * Lists all previous choosen directories in a new window.
     * Creates a new Stage to load FXMLDirectoryHistory view.
     * Listitems can be chosen to reload.
     */
    private void historyEvent(){
        Stage historyStage = new Stage(StageStyle.UTILITY);
        historyStage.setTitle("Basisverzeichnis auswählen");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791831/view/FXMLDirectoryHistory.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new FXMLDirectoryHistoryController(this,INPUT));
        
        /*load FXMLDirectoryHistory view an show in new Stage*/
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            historyStage.setScene(myScene);
            historyStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Loads directory chooser in a new window.
     * Creates a new Stage and a directory chooser from user.home.
     * If directory is chosen add it to ObservableList.
     */
    private void directoryChooserEvent(){
        Stage dcStage = new Stage();
        dcStage.setTitle("Open");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(HOME);
        File selectedDirectory = chooser.showDialog(dcStage);
        if(selectedDirectory == null){
            return;
        }
        FolderManagerIF fileManager = new FileManager(selectedDirectory);
        configureTree(fileManager);
        INPUT.add(selectedDirectory);
    }
    
    /**
     * Expandevent for expandable TreeItems.
     * Loads content of this TreeItem and changes icon.
     * @param event TreeModificationEvent to get clicked TreeItem
     * @param fm foldermanager to load content of clicked TreeItem
     */
    private void expandEvent(TreeModificationEvent<Component> event, FolderManagerIF fm){
        TreeItem<Component> item = event.getTreeItem();
        if(item.getValue() instanceof Folder ){
            fm.loadContent((Folder) item.getValue());
            showFolder(item);
            item.setGraphic(new ImageView(FOLDER_OPEN_ICON));
        }
    }
    
    /**
     * Collapseevent for expanded TreeItems.
     * Deletes all components of this TreeItem to avoid double with next expand and changes icon.
     * @param event TreeModificationEvent to get clicked TreeItem
     */
    private void collapseEvent(TreeModificationEvent<Component> event){
        TreeItem<Component> item = event.getTreeItem();
        if(item.getValue() instanceof Folder ){
            item.getValue().getComponents().clear();
            item.setGraphic(new ImageView(FOLDER_ICON));
        }
    }
    
    /**
     * Shows the content of this TeeItem in TreeView. 
     * @param treeItem TreeItem which should be expanded
     */
    public void showFolder(TreeItem<Component> treeItem){
        
        /*delete all children before adding new children - especially dummy TreeItem*/
        treeItem.getChildren().clear();

        List<Component> content = treeItem.getValue().getComponents();
        for(Component c: content){
            if(c instanceof Folder){
                TreeItem<Component> folder = new TreeItem<>(c,new ImageView(FOLDER_ICON));
                if(c.isExpandable()){
                    folder.getChildren().add(new TreeItem()); //dummy TreeItem makes TreeItem expandable
                }
                treeItem.getChildren().add(folder);
                
              /*undo comment if files should be visible*/
//            }else{
//                TreeItem<Component> fileElement = new TreeItem<>(c,new ImageView(DOCUMENT_ICON));
//                treeItem.getChildren().add(fileElement);
            }                
        }
    }


    
}