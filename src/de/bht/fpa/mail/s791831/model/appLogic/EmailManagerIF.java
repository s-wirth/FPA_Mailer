/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.data.Folder;

/**
 * This is the interface for classes that manage
 * emails.
 * @author Lena
 */
public interface EmailManagerIF {
    
    /**
     * Loads all relevant email content from the directory path of a folder
     * into the folder.
     * @param f the folder into which the content of the corresponding 
     *          directory should be loaded
     */
    void loadContent(Folder f); 
    
}
