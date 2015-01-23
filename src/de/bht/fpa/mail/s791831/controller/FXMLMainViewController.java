
package de.bht.fpa.mail.s791831.controller;

import de.bht.fpa.mail.s791831.model.appLogic.ApplicationLogicIF;
import de.bht.fpa.mail.s791831.model.appLogic.FacadeApplicationLogic;
import de.bht.fpa.mail.s791831.model.appLogic.imap.IMapConnectionHelper;
import de.bht.fpa.mail.s791831.model.data.Component;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.Store;

/**
 * This class is the controller of the FPA-Mailer. Used FXML document:
 * FXMLMainView.fxml Initializes all FPA-Mailer functions.
 *
 * @author Lena
 */
public class FXMLMainViewController implements Initializable {

    @FXML
    private TreeView<Component> treeView;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem fileOpen;
    @FXML
    private MenuItem fileSave;
    @FXML
    private MenuItem fileHistory;
    @FXML
    public Menu accountOpen;
    @FXML
    private Menu accountEdit;
    @FXML
    private MenuItem accountNew;
    @FXML
    private TextField searchBar;
    @FXML
    private Label countLabel;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn<Email, String> importance;
    @FXML
    private TableColumn<Email, String> received;
    @FXML
    private TableColumn<Email, String> read;
    @FXML
    private TableColumn<Email, String> sender;
    @FXML
    private TableColumn<Email, String> recipients;
    @FXML
    private TableColumn<Email, String> subject;
    @FXML
    private TextArea emailText;
    @FXML
    private Text senderText;
    @FXML
    private Text subjectText;
    @FXML
    private Text receivedText;
    @FXML
    private Text receiverText;
    
    /**
     * checks if listener was added to TreeView
     */
    private boolean showEmailListener = false;

    /**
     * ApplicationLogic
     */
    private final ApplicationLogicIF APP_LOGIC;

    /**
     * used icons for folder/files
     */
    private final Image FOLDER_ICON;
    private final Image FOLDER_OPEN_ICON;
//    private final Image DOCUMENT_ICON = new Image(getClass().getResourceAsStream("blue-document.png"));
   
    /**
     * saves all E-mails in chosen directory
     */
    private static final ObservableList<Email> EMAIL_CONTENT = FXCollections.observableArrayList();
    
    /**
     * Controller
     */
    public FXMLMainViewController() {
        APP_LOGIC = new FacadeApplicationLogic();
        FOLDER_ICON = new Image(getClass().getResourceAsStream("blue-folder.png"));
        FOLDER_OPEN_ICON = new Image(getClass().getResourceAsStream("blue-folder-open.png"));
    }

    /**
     * Initializes TreeView and Menu.
     *
     * @param url application URL
     * @param rb bundle for handling language translations
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureTree();
        configureAccounts();
        configureMenu();
        configureTableView();
        configureSearchField();
    }

    public void configureAccounts(){
        for (String name : APP_LOGIC.getAllAccounts()){
            accountOpen.getItems().add(new MenuItem(name));
            accountEdit.getItems().add(new MenuItem(name));
        }
    }
    
    /**
     * Creates a TreeView with root user.home. Adds events for expanding,
     * collapsing. Adds a Listener to select items.
     */
    public void configureTree(){

        APP_LOGIC.loadContent(APP_LOGIC.getTopFolder());
        TreeItem<Component> topFolder = new TreeItem<>(APP_LOGIC.getTopFolder(), new ImageView(FOLDER_OPEN_ICON));
        showFolder(topFolder);
        if (!topFolder.getChildren().isEmpty()) {
            topFolder.setExpanded(true);
        }

        /*add EventHandler to root - transfered to all TreeItems*/
        topFolder.addEventHandler(TreeItem.branchExpandedEvent(), (TreeModificationEvent<Component> e) -> expandEvent(e));
        topFolder.addEventHandler(TreeItem.branchCollapsedEvent(), (TreeModificationEvent<Component> e) -> collapseEvent(e));
        treeView.setRoot(topFolder);
        if (!showEmailListener) {
            showEmailListener = true;
            treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showEmail(newValue));
        }  
    }
    
    /**
     * Adds an event to all MenuItems.
     */
    public void configureMenu() {
        for (Menu m : menuBar.getMenus()) {
            for (MenuItem i : accountOpen.getItems()) {
                i.setOnAction((e) -> menuEvent(e));
            }
            for (MenuItem i : accountEdit.getItems()) {
                i.setOnAction((e) -> menuEvent(e));
            }
            for (MenuItem i : m.getItems()) {
                i.setOnAction((e) -> menuEvent(e));
            }
        }
    }
    
    /**
     * Adds a ChangeListener to the TableView.
     * Calls showEmailContent.
     */
    private void configureTableView(){
        importance.setCellValueFactory(new PropertyValueFactory<>("importance"));
        received.setCellValueFactory(new PropertyValueFactory<>("received"));
        
        /* default column sort by date */
        received.setComparator((date1,date2) -> compare(date1,date2));
        
        read.setCellValueFactory(new PropertyValueFactory<>("read"));
        sender.setCellValueFactory(new PropertyValueFactory<>("sender"));
        recipients.setCellValueFactory(new PropertyValueFactory<>("receiverTo"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        
        tableView.getSortOrder().add(received);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
                                                                                showEmailContent((Email)newValue));
    }
    
    /**
     * configures search field, adds listener
     */
    private void configureSearchField(){
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchEmails(newValue));
    }
    
    /**
     * filters all emails in tableView
     * @param newValue the filter
     */
    private void searchEmails(String newValue) {
        List<Email> list = APP_LOGIC.search(EMAIL_CONTENT, newValue);
        ObservableList fill = FXCollections.observableArrayList();
        for(Email e: list){
            fill.add(e);
        }
        tableView.setItems(fill);
        countLabel.setText("(" + fill.size() + ")");
    }
    
    /**
     * Shows information (text, sender, subject, receiver, received) from selected E-Mail.
     * @param email selected E-Mail to show information for
     */
    private void showEmailContent(Email email) {
        if (email == null) {
            emailText.setText("");
            senderText.setText("");
            subjectText.setText("");
            receiverText.setText("");
            receivedText.setText("");
            return;
        }
        emailText.setText(email.getText());
        senderText.setText(email.getSender());
        subjectText.setText(email.getSubject());
        receiverText.setText(email.getReceiver());
        receivedText.setText(email.getReceived());   
    }

    /**
     * Searches for E-Mails in the content of the selected TreeItem. Calls method
     * to fill TableView. Prints on console: - path of the selected folder -
     * number of E-Mails in selected folder - sender, receiving date and subject
     * of each E-Mail
     *
     * @param item selected TreeItem
     */
    private void showEmail(TreeItem<Component> item) {
        if (item == null) {
            return;
        }
        Folder folder = (Folder) item.getValue();
        EMAIL_CONTENT.clear();
        APP_LOGIC.loadEmails(folder);
        
        /*Set new Graphic to item to show number of emails instantly*/
        if(item.isExpanded()){
            item.setGraphic(new ImageView(FOLDER_OPEN_ICON));
        }else{
            item.setGraphic(new ImageView(FOLDER_ICON));
        }
        
        System.out.println("Selected directory: " + item.getValue().getPath());
        System.out.println("Number of emails: " + folder.getEmails().size());
        for (Email email : folder.getEmails()) {
            System.out.println(email.toString());
            EMAIL_CONTENT.add(email);
        }

        fillTableView(EMAIL_CONTENT);
    }

    /**
     * Fills TableView with content from E-Mails. (importance, received, read,
     * sender, recipients, subject)
     */
    private void fillTableView(ObservableList content) {
        tableView.setItems(content);
        searchBar.clear();
        countLabel.setText("");
    }

    /**
     * Decides which event should be triggered by selected MenuItem.
     *
     * @param e Event to get clicked MenuItem
     */
    private void menuEvent(Event e) {
        if (e.getSource() == fileOpen) {
            directoryChooserEvent();
        }
        else if (e.getSource() == fileHistory) {
            historyEvent();
        }
        else if (e.getSource() == fileSave){
            saveDirectoryChooserEvent();
        }
        else if(e.getSource() == accountNew){
            newAccountEvent();
        }else{
            for(MenuItem item: accountEdit.getItems()){
                if(e.getSource() == item){
                    editAccountEvent(item.getText());
                }
            }
            for(MenuItem item: accountOpen.getItems()){
                if(e.getSource() == item){
                    openAccountEvent(item);
                }
            }
        }    
    }

    /**
     * Lists all previous chosen directories in a new window. Creates a new
     * Stage to load FXMLDirectoryHistory view. ListItems can be chosen to
     * reload.
     */
    private void historyEvent() {
        Stage historyStage = new Stage(StageStyle.UTILITY);
        historyStage.setTitle("Basisverzeichnis auswählen");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791831/view/FXMLDirectoryHistory.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new FXMLDirectoryHistoryController(this));

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
     * Calls changeDirectory from appLogic to do the following:
     * Loads directory chooser in a new window. Creates a new Stage and a
     * directory chooser from user.home. If directory is chosen add it to
     * ObservableList.
     */
    private void directoryChooserEvent() {
        Stage dcStage = new Stage();
        dcStage.setTitle("Open");
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(FacadeApplicationLogic.HOME);
        File selectedDirectory = chooser.showDialog(dcStage);
        if (selectedDirectory != null) {
            APP_LOGIC.changeDirectory(selectedDirectory);
            configureTree();
        }
    }

    /**
     * directoryChooserEvent
     */
    private void saveDirectoryChooserEvent() {
        Stage dcsStage = new Stage();
        /* Title the Stage */
        dcsStage.setTitle("Save");
        /* FileChooser initialised */
        FileChooser chooser = new FileChooser();
        /* name of the file and file extension which shall be saved */
        chooser.setInitialFileName("mail.xml");
        /* get file the user is trying to save, empty file with path and name */
        File selectedDirectory = chooser.showSaveDialog(dcsStage);
        if (selectedDirectory != null) {
            APP_LOGIC.saveEmails(EMAIL_CONTENT, selectedDirectory);
        }
    }
    
    /**
     * creates a new Account
     */
    private void newAccountEvent() {
        Stage newAccountStage = new Stage(StageStyle.UTILITY);
        newAccountStage.setTitle("New Account");
        URL location = getClass().getResource("/de/bht/fpa/mail/s791831/view/FXMLNewAccount.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setController(new FXMLNewAccountController(this));

        /*load FXMLNewAccount view an show in new Stage*/
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            newAccountStage.setScene(myScene);
            newAccountStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * ExpandEvent for expandable TreeItems. Loads content of this TreeItem and
     * changes icon.
     *
     * @param event TreeModificationEvent to get clicked TreeItem
     */
    private void expandEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> item = event.getTreeItem();
        if (item.getValue() instanceof Folder) {
            APP_LOGIC.loadContent((Folder) item.getValue());
            showFolder(item);
            item.setGraphic(new ImageView(FOLDER_OPEN_ICON));
        }
    }

    /**
     * CollapseEvent for expanded TreeItems. Deletes all components of this
     * TreeItem to avoid double with next expand and changes icon.
     *
     * @param event TreeModificationEvent to get clicked TreeItem
     */
    private void collapseEvent(TreeModificationEvent<Component> event) {
        TreeItem<Component> item = event.getTreeItem();
        if (item.getValue() instanceof Folder) {
            item.getValue().getComponents().clear();
            item.setGraphic(new ImageView(FOLDER_ICON));
        }
    }

    /**
     * Shows the content of this TeeItem in TreeView.
     *
     * @param treeItem TreeItem which should be expanded
     */
    private void showFolder(TreeItem<Component> treeItem) {

        /*delete all children before adding new children - especially dummy TreeItem*/
        treeItem.getChildren().clear();

        List<Component> content = treeItem.getValue().getComponents();
        for (Component c : content) {
            if (c instanceof Folder) {
                TreeItem<Component> folder = new TreeItem<>(c, new ImageView(FOLDER_ICON));
                if (c.isExpandable()) {
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

    /**
     * Getter for ObservableList INPUT
     *
     * @return ObservableList with directory input
     */
    public ObservableList getINPUT() {
        return FacadeApplicationLogic.INPUT;
    }

    /**
     * Getter for appLogic
     *
     * @return this.appLogic
     */
    public ApplicationLogicIF getAppLogic() {
        return APP_LOGIC;
    }

    /**
     * converts strings from received column to dates, to be sorted by date
     * @param s1 string1 to be compared as date
     * @param s2 string2 to be compared as date
     * @return sorted dates
     */
    private int compare(String s1, String s2) {
        DateFormat FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.GERMANY);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = FORMAT.parse(s1);
            date2 = FORMAT.parse(s2);
        } catch (ParseException ex2) {
        }
        return date1.compareTo(date2);
    }

    private void openAccountEvent(MenuItem item) {
        String name = item.getText();
        APP_LOGIC.setTopFolder(new File(APP_LOGIC.getAccount(name).getTop().getPath()));
        Store connect = IMapConnectionHelper.connect(APP_LOGIC.getAccount(name));
        configureTree();
    }

    private void editAccountEvent(String name) {
        Stage editAccountStage = new Stage(StageStyle.UTILITY);
        editAccountStage.setTitle("Edit Account");
  
        URL location = getClass().getResource("/de/bht/fpa/mail/s791831/view/FXMLNewAccount.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        FXMLNewAccountController controller = new FXMLNewAccountController(this);
        fxmlLoader.setController(controller);

        /*load FXMLNewAccount view an show in new Stage*/
        try {
            Pane myPane = (Pane) fxmlLoader.load();
            Scene myScene = new Scene(myPane);
            editAccountStage.setScene(myScene);
            controller.setEdit(APP_LOGIC.getAccount(name));
            editAccountStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
