package de.twenty11.skysail.server.ext.mail.accounts;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.server.forms.ListView;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = { "title" })
//@JsonPropertyOrder({ "title", "desc" })
@NoArgsConstructor
public class Account implements Serializable, Identifiable {

    private static final long serialVersionUID = -5632636376979025266L;

    @Id
    private String id;

    @Field
    private String host;

    @Field
    private String user;

    @Field
    private String pass;
    
    @Field(type = InputType.READONLY)
    @ListView(hide=true)
    private String owner;

    public Account(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

}
