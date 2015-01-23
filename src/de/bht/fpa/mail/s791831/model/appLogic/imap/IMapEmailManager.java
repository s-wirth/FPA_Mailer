package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.appLogic.EmailManagerIF;
import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
import javafx.collections.ObservableList;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList filterEmails(ObservableList<Email> emailList, String pattern) {
        return null;
    }

    @Override
    public void saveEmails(ObservableList<Email> emailList, File file) {
      
    }
    
}
