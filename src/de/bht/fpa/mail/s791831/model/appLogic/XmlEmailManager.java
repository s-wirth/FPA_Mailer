/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

/**
 * This class manages the email content of a given folder.
 * @author Lena
 */
public class XmlEmailManager implements EmailManagerIF{

    @Override
    public void loadContent(Folder f) {
        File file = new File (f.getPath());
        File[] content = file.listFiles();
        if(content != null){
            
            /*loop through content filters all appropriate emails and adds them to given Folder*/
            for(File c: content){
                if(c.isFile() && c.getName().endsWith(".xml")){
                    try{
                        f.addEmail(JAXB.unmarshal(c, Email.class));
                    } catch (DataBindingException e){
                        System.out.println("unconform XML: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    @Override
    public ObservableList filterEmails(ObservableList<Email> emailList, String pattern) {
        ObservableList filtered = FXCollections.observableArrayList();
        for(Email e: emailList){
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
    
    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {

        String path = file.getAbsolutePath();
        
        /* int for numbering the name of files, so that multiple files can be saved */
        int i = 1;
        /* loop through all the emails in f */
        for(Email mail : emailList){
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
