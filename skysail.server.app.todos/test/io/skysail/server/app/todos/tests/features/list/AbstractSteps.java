package io.skysail.server.app.todos.tests.features.list;

import java.util.Arrays;

import org.restlet.data.Form;

public class AbstractSteps {

    protected Form createForm(String input) {
        Form form = new Form();
        if (input.trim().length() > 0) {
            String[] splits = input.split("&");
            Arrays.stream(splits).forEach(s -> {
                String[] keyValuePair = s.split("=");
                form.add(keyValuePair[0], keyValuePair[1]);
            });
        }
        return form;
    }

}
