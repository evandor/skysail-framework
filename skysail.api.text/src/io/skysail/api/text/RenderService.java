package io.skysail.api.text;

import org.restlet.resource.Resource;

public interface RenderService {

    String translate(String key, String defaultMsg, Resource resource, Object... subsitutions);
}
