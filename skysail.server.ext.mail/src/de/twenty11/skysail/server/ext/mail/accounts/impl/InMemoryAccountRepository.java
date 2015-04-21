//package de.twenty11.skysail.server.ext.mail.accounts.impl;
//
//import java.util.List;
//
//import de.twenty11.skysail.server.ext.mail.accounts.Account;
//
//public class InMemoryAccountRepository implements AccountRepository {
//
//    private InMemoryRepository<Account> repo = new InMemoryRepository<Account>();
//
//    @Override
//    public void add(Account entity) {
//        repo.add(entity);
//    }
//
//    @Override
//    public Account getById(String accountId) {
//        return repo.getById(accountId);
//    }
//
//    @Override
//    public List<Account> getAll() {
//        return repo.getAll();
//    }
//
//}
