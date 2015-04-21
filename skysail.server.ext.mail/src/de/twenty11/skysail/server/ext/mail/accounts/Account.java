package de.twenty11.skysail.server.ext.mail.accounts;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.io.Serializable;

public class Account implements Identifiable, Serializable {

    private static final long serialVersionUID = 3277770588774698115L;

    private String id;

    @Field
    private String host;

    @Field
    private String user;

    @Field
    private String pass;

    public Account(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

    public Account() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

//    @Override
//    public List<Linkheader> getLinkheader() {
//        List<Linkheader> links = new ArrayList<>();
//        links.add(new Linkheader.Builder("mail/accounts/" + getId() + "/folders").relation(LinkHeaderRelation.COLLECTION).title("folders").build());
//
//        return links;
//    }

}
