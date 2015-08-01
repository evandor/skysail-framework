package de.twenty11.skysail.server.ext.mail.accounts;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.ListView;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@ToString(of = { "id", "host", "user" })
//@JsonPropertyOrder({ "title", "desc" })
@NoArgsConstructor
public class Account implements Serializable, Identifiable {

    private static final long serialVersionUID = -5632636376979025266L;

    @Id
    private String id;

    @Field
    private String host;

    @Field
    private Integer port;

    @Field
    private String user;

    @Field(inputType = InputType.PASSWORD)
    @ListView(hide = true)
    private String pass;
    
    @Field(inputType = InputType.READONLY)
    @ListView(hide=true)
    private String owner;

    public Account(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
    }

}
