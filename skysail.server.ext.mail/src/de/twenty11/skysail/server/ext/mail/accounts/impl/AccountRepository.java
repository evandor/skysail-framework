package de.twenty11.skysail.server.ext.mail.accounts.impl;

import java.util.List;

import de.twenty11.skysail.server.ext.mail.accounts.Account;

public interface AccountRepository {

    void add(Account entity);

    Account getById(String accountId);

    List<Account> getAll();

}
