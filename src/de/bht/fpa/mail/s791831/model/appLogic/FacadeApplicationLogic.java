
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.appLogic.account.AccountManager;
import de.bht.fpa.mail.s791831.model.appLogic.account.AccountManagerIF;
import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Lena
 */
public class FacadeApplicationLogic implements ApplicationLogicIF{
    
    /**
     * file manager
     */
    private FolderManagerIF fileManager;
    
    /**
     * email manager
     */
    private final EmailManagerIF emailManager; 
    
    /**
     * account manager
     */
    private final AccountManagerIF accountManager;
    
    /**
     * home directory
     */
    public static final File HOME = new File(System.getProperty("user.home"));
    
    /**
     * saves all directories chosen in history
     */
    public static final ObservableList INPUT = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public FacadeApplicationLogic() {
        fileManager= new FileManager(HOME);
        emailManager = new XmlEmailManager();
        accountManager = new AccountManager();
    }
 
    @Override
    public Folder getTopFolder() {
        return fileManager.getTopFolder(); 
    }

    @Override
    public void loadContent(Folder folder) { 
        fileManager.loadContent(folder);
    }

    @Override
    public List<Email> search(ObservableList<Email> emailList, String pattern) {
        return emailManager.filterEmails(emailList, pattern);
    }

    @Override
    public void loadEmails(Folder folder) {
        folder.getEmails().clear();
        emailManager.loadContent(folder);
    }

    // directoryChooser Event
    @Override
    public void changeDirectory(File file) {
        fileManager = new FileManager(file);
        
        /*if case to avoid double entries, remove directory from history to add it to the end of the list*/
        if (INPUT.contains(file)) {
            INPUT.remove(file);
        }
        INPUT.add(file);
    }

    // Save menuItem
    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {
        emailManager.saveEmails(emailList, file);
    }
    
    @Override
    public void setTopFolder(File file){
        fileManager = new FileManager(file);
    }

    @Override
    public void openAccount(String name) {
        Account acc = getAccount(name);
        File file = new File (acc.getTop().getPath());
        setTopFolder(file);
    }

    @Override
    public List<String> getAllAccounts() {
        List<String> accountNames = new ArrayList<>();
        for (Account acc: accountManager.getAllAccounts()){
            accountNames.add(acc.getName());
        }
        return accountNames;
    }

    @Override
    public Account getAccount(String name) {
        return accountManager.getAccount(name);
    }

    @Override
    public boolean saveAccount(Account account) {
        return accountManager.saveAccount(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountManager.updateAccount(account);
    }
}
