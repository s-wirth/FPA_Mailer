/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 *
 * @author Lena
 */
public class FacadeApplicationLogic implements ApplicationLogicIF{
    
    private FolderManagerIF fileManager;
    private final EmailManagerIF emailManager;
    private final MenuLogic menuLogic;
    
    
    public ObservableList<Email> filteredEmailContent = FXCollections.observableArrayList();

    
    
    /**
     * home directory
     */
    public static final File HOME = new File(System.getProperty("user.home"));
    
    /**
     * saves all directories chosen in history
     */
    public static final ObservableList INPUT = FXCollections.observableArrayList();
    
    /**
     * saves all E-mails in chosen directory
     */
    public static final ObservableList<Email> EMAIL_CONTENT = FXCollections.observableArrayList();
    
    public FacadeApplicationLogic() {
        fileManager= new FileManager(HOME);
        emailManager = new XmlEmailManager();
        menuLogic = new MenuLogic();
        filteredEmailContent.addAll(EMAIL_CONTENT);
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
    public List<Email> search(String pattern) {
        filteredEmailContent = menuLogic.filterEmails(pattern);
        return filteredEmailContent;
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
    public void saveEmails(File file) {
        menuLogic.saveEmails(file);
    }
    
    @Override
    public void setTopFolder(File file){
        fileManager = new FileManager(file);
    }
}
