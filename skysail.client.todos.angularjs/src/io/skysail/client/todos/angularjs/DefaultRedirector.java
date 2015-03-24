package io.skysail.client.todos.angularjs;

import org.restlet.Context;
import org.restlet.routing.Redirector;

public class DefaultRedirector extends Redirector {

    public DefaultRedirector(Context context, String targetTemplate) {
        super(context, targetTemplate);
    }

}
