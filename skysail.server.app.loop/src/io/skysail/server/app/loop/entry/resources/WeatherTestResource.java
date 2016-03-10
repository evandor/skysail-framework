package io.skysail.server.app.loop.entry.resources;

import java.util.*;

import io.skysail.server.restlet.resources.ListServerResource;

public class WeatherTestResource extends ListServerResource<Weather>{

    @Override
    public List<?> getEntity() {
        return Collections.emptyList();
    }

}
