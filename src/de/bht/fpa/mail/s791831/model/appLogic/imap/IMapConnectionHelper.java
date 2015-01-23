package de.bht.fpa.mail.s791831.model.appLogic.imap;


import de.bht.fpa.mail.s791831.model.data.Account;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.AuthenticationFailedException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * This class contains a Method to connect to an IMAP email server. 
 * @author Simone Strippgen
 */

public class IMapConnectionHelper {
    private static final String JAVA_MAIL_IMAPS = "imaps";
    private static final String JAVA_MAIL_STORE_PROTOCOL = "mail.store.protocol"; 
    
    
    public IMapConnectionHelper(){
        
    }
    
    
    /**
     * Connects to an IMAP email server using the information in the given account.
     * @param acc the account which contains the connection information
     * @return the Store object for loading folders and E-mails
     */
    public static Store connect(Account acc) {
        try {
                Properties props = System.getProperties();
                props.setProperty(JAVA_MAIL_STORE_PROTOCOL, JAVA_MAIL_IMAPS);
                Session session = Session.getInstance(props);
                Store store = session.getStore(JAVA_MAIL_IMAPS);
                store.connect(acc.getHost(), acc.getUsername(), acc.getPassword());
                System.out.println("Connection: " + store.toString() + " connected: " + store.isConnected());
                return store;
        } catch (final AuthenticationFailedException ex) {
            System.out.println("Authentifizierung ist fehlgeschlagen.");
        } catch (Exception ex) {
            Logger.getLogger(IMapConnectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
