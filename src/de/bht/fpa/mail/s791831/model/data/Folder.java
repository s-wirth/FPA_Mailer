package de.bht.fpa.mail.s791831.model.data;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Simone Strippgen
 */
public class Folder extends Component {

    private final boolean expandable;
    private final ArrayList<Component> content;
    private final ArrayList<Email> emails;
    
    /**
     * Initializes new Folder.
     * @param path directory path for Folder
     * @param expandable desides whether this Folder is expandable
     */
    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<>();
        emails = new ArrayList<Email>();
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    /**
     * Gets a List of all emails contained by this folder.
     * @return a List with all emails 
     */
    public List<Email> getEmails() {
        return emails;
    }

    /**
     * Adds an email to the email content of this Folder.
     * @param message email that is added
     */
    public void addEmail(Email message) {
        emails.add(message);
    }
    
    @Override
    public String toString(){
        if(emails.size() == 0){
            return this.getName();
        }
        return this.getName() + " (" + emails.size() + ")";
    }
 }
