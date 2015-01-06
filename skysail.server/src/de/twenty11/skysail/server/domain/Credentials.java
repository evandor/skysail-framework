package de.twenty11.skysail.server.domain;

import javax.validation.constraints.Size;

import de.twenty11.skysail.api.forms.Field;
//import de.twenty11.skysail.api.forms.Form;
import de.twenty11.skysail.api.forms.InputType;

//@Form(name = "credentials")
public class Credentials {

    @Size(min = 3, message = "Username must have at least three characters")
    @Field
    private String username;

    @Size(min = 3, message = "Password must have at least three characters")
    @Field(type = InputType.PASSWORD)
    private String password;

    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username.replace("@", "&#64;");
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
