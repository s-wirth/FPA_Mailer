/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.appLogic.FolderManagerIF;
import de.bht.fpa.mail.s791831.model.data.Account;
import de.bht.fpa.mail.s791831.model.data.Folder;

/**
 *
 * @author sophie
 */
public class IMapFolderManager implements FolderManagerIF {
    
    public IMapFolderManager(Account acc){
        
    }

    @Override
    public Folder getTopFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadContent(Folder f) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
