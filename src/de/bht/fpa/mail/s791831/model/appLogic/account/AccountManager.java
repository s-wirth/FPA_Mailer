/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.account;

import de.bht.fpa.mail.s791831.model.data.Account;
import java.util.List;

/**
 *
 * @author Lena
 */
public class AccountManager implements AccountManagerIF{

//    private final AccountDAOIF accountDAO;
    
    private final AccountDBDAO accountDBDAO;
    
    /**
     * Constructor
     */
    public AccountManager(){
        accountDBDAO = new AccountDBDAO();
    }
    
    @Override
    public Account getAccount(String name) {
        for(Account a: accountDBDAO.getAllAccounts()){
            if (a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDBDAO.getAllAccounts();
    }

    @Override
    public boolean saveAccount(Account acc) {
        for(Account a: accountDBDAO.getAllAccounts()){
            if (a.getName().equals(acc.getName())){
                return false;
            }
        }
        accountDBDAO.saveAccount(acc);
        return true;
    }

    @Override
    public boolean updateAccount(Account account) {
        return accountDBDAO.updateAccount(account);
    }
    
}
