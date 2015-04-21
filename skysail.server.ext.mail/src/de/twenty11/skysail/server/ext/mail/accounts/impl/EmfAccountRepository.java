package de.twenty11.skysail.server.ext.mail.accounts.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import de.twenty11.skysail.server.ext.mail.MailApplication;
import de.twenty11.skysail.server.ext.mail.accounts.Account;

public class EmfAccountRepository implements AccountRepository {

    private MailApplication app;
    private EntityManager entityManager;

    public EmfAccountRepository(EntityManagerFactory emf, MailApplication app) {
        this.entityManager = emf.createEntityManager();
        this.app = app;
    }

    @Override
    public void add(Account entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public Account getById(String accountId) {
        return null;
    }

    @Override
    public List<Account> getAll() {
        // TODO Auto-generated method stub
        return null;
    }

}
