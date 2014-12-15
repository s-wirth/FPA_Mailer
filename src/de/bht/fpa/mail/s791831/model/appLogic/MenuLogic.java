/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.data.Email;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXB;

/**
 * Implements all Menu-Events
 * @author Lena
 */
public class MenuLogic {
    
    /**
     * filter all emails in list
     * @param pattern to search for
     * @return filtered list
     */
    public ObservableList filterEmails(String pattern) {
        ObservableList filtered = FXCollections.observableArrayList();
        for(Email e: FacadeApplicationLogic.EMAIL_CONTENT){
            if(matchesFilter(e,pattern)){
                filtered.add(e);
            }
        }
        return filtered;
    }
    
    /**
     * looks if pattern is in email
     * @param email to be searched
     * @param pattern to search for
     * @return boolean if pattern is in email or not
     */
    private boolean matchesFilter(Email email, String pattern){
        // If filter text is empty, display all emails.
        if (pattern == null || pattern.isEmpty()) {
            return true;
        }

        // Compare subject, text, received, sent, receiver and sender of every email with filter text.
        String lowerCaseFilter = pattern.toLowerCase();
//        countLabel.setText(String.valueOf("(" + filteredEmailContent.size()) + ")");
        if (email.getSubject().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches subject.
        } else if (email.getText().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches text.
        } else if (email.getReceived().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches received.
        } else if (email.getSent().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches sent.
        } else if (email.getReceiver().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches receiver.
        } else if (email.getSender().toLowerCase().contains(lowerCaseFilter)) {
            return true; // Filter matches sender.
        }
        return false; // Does not match.
    }
    
    /**
     * saves xml files in given file
     * @param file where emails should be saved
     */
    public void saveEmails(File file) {

        String path = file.getAbsolutePath();
        
        /* int for numbering the name of files, so that multiple files can be saved */
        int i = 1;
        /* loop through all the emails in f */
        for(Email mail : FacadeApplicationLogic.EMAIL_CONTENT){
           /* split name of file on extension */
           String[] nameParts = path.split("\\.");
           /* new Name consisting of original file name and int i for numbering
           /* could be changed later, so instead of i make the title into the subject (i.e.) */
           String newName = nameParts[0] + i + "." + nameParts[1];
           /* new file with new name */
           File newFile = new File(newName);
           i++;
           /* marshal makes into real file, containing the mail into the newly created file and write it on the harddisk */
           JAXB.marshal(mail, newFile); 
        }
    }
    
}
