/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.imap;

import de.bht.fpa.mail.s791831.model.data.Folder;
import javax.mail.MessagingException;

/**
 *
 * @author sophie
 */
public class IMapFolder extends Folder{
    
    private javax.mail.Folder origin;
    
    public IMapFolder(javax.mail.Folder origin) throws MessagingException{
        super();
        this.origin = origin;
        setName(origin.getName());
        if((origin.getType() & javax.mail.Folder.HOLDS_FOLDERS) != 0){
            if(origin.list().length > 0){
                this.setExpandable(true);
            }
        }
    }

    public javax.mail.Folder getOrigin() {
        return origin;
    }
    
}
