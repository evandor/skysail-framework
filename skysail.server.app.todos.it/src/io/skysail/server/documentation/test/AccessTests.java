package io.skysail.server.documentation.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.Client;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

public class AccessTests extends IntegrationTests {

    @Before
    public void setUp() throws Exception {
        client = new Client(getBaseUrl());
        form = new Form();
    }

    @Test
    public void demoUser_doesnot_see_admins_todos() throws IOException {
        client.loginAs("admin", "skysail");
        form.add("title", "adminstodo");
        client.setUrl("/Todos/Todos/").post(form, MediaType.TEXT_HTML);

        client.loginAs("demo", "skysail");
        Representation representation = client.setUrl("/Todos/Todos").get(MediaType.APPLICATION_JSON);
        String text = representation.getText();
        System.out.println(text);
        assertThat(text, not(containsString("adminstodo")));
    }
}