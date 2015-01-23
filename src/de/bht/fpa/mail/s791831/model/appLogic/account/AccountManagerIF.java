package de.bht.fpa.mail.s791831.model.appLogic.account;


import de.bht.fpa.mail.s791831.model.data.Account;
import java.util.List;

/*
 * This is the interface for classes that manage
 * accounts.
 * 
 * @author Simone Strippgen
 */
public interface AccountManagerIF {

    /**
     * Returns the account with the given name.
     * @return null If no account with this name exists.
     * @param name  name of the account 
     */
    Account getAccount(String name);

    /**
     * @return a list of all account names.
     */
    List<Account> getAllAccounts();

    /**
     * Saves the given Account in the data store, if an account
     * with the given name does not exist.
     * @return  false, if an account with this name
     * already exists.
     * @param acc  the account that should be saved
     */

    boolean saveAccount(Account acc);

    /**
     * Updates the given Account in the data store.
     * @param account  the account that should be updated
     * @return true if update was successful.
     */
    boolean updateAccount(Account account);
    
}