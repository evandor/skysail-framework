package io.skysail.server.app.intro.topics;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class TopicsResource extends ListServerResource<Topic> {

    @Override
    public List<Topic> getData() {
        return Collections.emptyList();
    }

}
