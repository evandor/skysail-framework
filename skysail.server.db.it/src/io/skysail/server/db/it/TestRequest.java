package io.skysail.server.db.it;

import org.restlet.Request;
import org.restlet.data.Reference;

public class TestRequest extends Request {

    @Override
    public Reference getResourceRef() {
        return new Reference();
    }
}
