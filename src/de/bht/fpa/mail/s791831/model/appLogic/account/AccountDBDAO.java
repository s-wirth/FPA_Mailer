/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791831.model.appLogic.account;

import de.bht.fpa.mail.s791831.model.data.Account;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
/**
 *
 * @author sophie
 */
public class AccountDBDAO implements AccountDAOIF {
    
    EntityManagerFactory emf;
    public AccountDBDAO () {
        this.emf = Persistence.createEntityManagerFactory("FPA_MailerPU");
        
    }

    @Override
    public List<Account> getAllAccounts() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT e FROM Account e");
        return (List<Account>) query.getResultList();
    }

    @Override
    public Account saveAccount(Account acc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        if(acc != null){
           trans.begin();
            em.persist(acc);
            trans.commit();
            em.close();
        }
        return acc;
    }

    @Override
    public boolean updateAccount(Account acc) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction trans = em.getTransaction();
         if (acc != null) {
            trans.begin();
            em.merge(acc);
            trans.commit();
            em.close();
        }
        return true;
    }
    
}
