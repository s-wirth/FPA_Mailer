/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.appLogic.FolderManagerIF;
import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
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
        javax.mail.Folder storeFolder;
        
        try {
            storeFolder = store.getDefaultFolder();
            IMapFolder topFolder = new IMapFolder(storeFolder);
            return topFolder;
        } catch (MessagingException ex) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public void loadContent(Folder f) {
        IMapFolder imapFolder = (IMapFolder) f;
        javax.mail.Folder storeFolder = imapFolder.getOrigin();
        
        
        try {
            
            javax.mail.Folder[] storeFolderChildren = storeFolder.list();
            for (javax.mail.Folder storeFolderChild : storeFolderChildren) {
                Folder child = new IMapFolder(storeFolderChild);
                imapFolder.addComponent(child);
            }
        } catch (MessagingException ex) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}
