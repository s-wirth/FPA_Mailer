/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.appLogic.FolderManagerIF;
import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author sophie
 */
public class IMapFolderManager implements FolderManagerIF {
    
    private Store store;
    
    private Account account;
    
    public IMapFolderManager(Account account){
        store = IMapConnectionHelper.connect(account);
        this.account = account;
        
    }

    @Override
    public Folder getTopFolder() {
        Folder topFolder = new Folder();
        javax.mail.Folder storeFolder;
        
        try {
            storeFolder = store.getDefaultFolder();
            topFolder.setName(storeFolder.getName());
            return topFolder;
        } catch (MessagingException ex) {
            System.out.println("Your. Shit. Is. On. Fiiiiiiiire!");
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public void loadContent(Folder f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
