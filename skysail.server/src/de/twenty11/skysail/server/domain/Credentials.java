package de.twenty11.skysail.server.domain;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import javax.validation.constraints.Size;

//import io.skysail.api.forms.Form;

//@Form(name = "credentials")
public class Credentials implements Identifiable {

    @Size(min = 3, message = "Username must have at least three characters")
    @Field
    private String username;

    @Size(min = 3, message = "Password must have at least three characters")
    @Field(inputType = InputType.PASSWORD)
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

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
