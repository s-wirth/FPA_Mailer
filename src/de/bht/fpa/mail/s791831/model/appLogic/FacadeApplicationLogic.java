
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.appLogic.account.AccountManager;
import de.bht.fpa.mail.s791831.model.appLogic.account.AccountManagerIF;
import de.bht.fpa.mail.s791831.model.appLogic.imap.IMapConnectionHelper;
import de.bht.fpa.mail.s791831.model.appLogic.imap.IMapFolderManager;
import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.mail.Store;

/**
 *
 * @author Lena
 */
public class FacadeApplicationLogic implements ApplicationLogicIF{
    
    /**
     * file manager
     */
    private FolderManagerIF imapFolderManager;
    
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
    
    private Account activeAccount;

    /**
     * Constructor
     */
    public FacadeApplicationLogic() {
        emailManager = new XmlEmailManager();
        accountManager = new AccountManager();
    }
 
    @Override
    public Folder getTopFolder() {
        if(imapFolderManager == null){
            return null;
        }
        return imapFolderManager.getTopFolder(); 
    }

    @Override
    public void loadContent(Folder folder) { 
        imapFolderManager.loadContent(folder);
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
//        imapFolderManager = new FileManager(file);
//        
//        /*if case to avoid double entries, remove directory from history to add it to the end of the list*/
//        if (INPUT.contains(file)) {
//            INPUT.remove(file);
//        }
//        INPUT.add(file);
    }

    // Save menuItem
    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {
        emailManager.saveEmails(emailList, file);
    }
    
//    @Override
    public void setTopFolder(File file){
//        imapFolderManager = new IMapFolderManager(file);
    }

    @Override
    public void openAccount(String name) {
        activeAccount = getAccount(name);
        imapFolderManager = new IMapFolderManager(activeAccount);
        
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
