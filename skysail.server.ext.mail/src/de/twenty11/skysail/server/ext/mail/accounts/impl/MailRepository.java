package de.twenty11.skysail.server.ext.mail.accounts.impl;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.db.DbService;

import java.util.*;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.ext.mail.accounts.Account;
import de.twenty11.skysail.server.ext.mail.mails.Mail;

@Component(immediate = true, properties = "name=MailRepository")
public class MailRepository implements DbRepository {

    private static DbService dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", Account.class.getSimpleName());
        dbService.register(Account.class);
       // dbService.createUniqueIndex(Account.class, "owner");
        dbService.createWithSuperClass("V", Mail.class.getSimpleName());
        dbService.register(Mail.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        MailRepository.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        MailRepository.dbService = null;
    }

    public <T> List<T> findAll(Class<T> cls) {
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        String sql = "SELECT from " + cls.getSimpleName() + " WHERE owner= :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return dbService.findObjects(sql, params);
    }

    public static Object add(Object entity, String... edges) {
        return dbService.persist(entity, edges);
    }

    public Account findById(Class<Account> cls, String id) {
        return dbService.findObjectById(cls, id);
    }

    public Object update(String id, Object entity, String... edges) {
        return dbService.update(id, entity);
    }

    @Override
    public Object save(Identifiable identifiable) {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }


}