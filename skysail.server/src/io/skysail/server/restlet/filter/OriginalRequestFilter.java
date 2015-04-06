package io.skysail.server.restlet.filter;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;

public class OriginalRequestFilter extends Filter {

    public OriginalRequestFilter(Context context) {
        super(context);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        return CONTINUE;
    }

}
