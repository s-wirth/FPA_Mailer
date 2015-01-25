package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s791831.model.appLogic.imap.IMapEmailConverter;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javax.mail.Message;
import javax.mail.MessagingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sophie
 */
public class IMapEmailManager implements EmailManagerIF {

    @Override
    public void loadContent(Folder f) {
        IMapFolder imapFolder = (IMapFolder) f;
        javax.mail.Folder javaxFolder = imapFolder.getOrigin();
        try {
            if((javaxFolder.getType() & javax.mail.Folder.HOLDS_MESSAGES) == 0){
                return;
            }
            if(!javaxFolder.isOpen()){
                javaxFolder.open(javax.mail.Folder.READ_ONLY);
            }
            Message[] javaxMessages = javaxFolder.getMessages();
            for(Message message : javaxMessages){
                Email email = IMapEmailConverter.convertMessage(message);
                imapFolder.addEmail(email);
                
            }
        } catch (MessagingException ex) {
            Logger.getLogger(IMapEmailManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ObservableList filterEmails(ObservableList<Email> emailList, String pattern) {
        return null;
    }

    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {
      
    }
    
}
