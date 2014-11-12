/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic;

import de.bht.fpa.mail.s791831.model.data.Email;
import de.bht.fpa.mail.s791831.model.data.Folder;
import java.io.File;
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
    
}
