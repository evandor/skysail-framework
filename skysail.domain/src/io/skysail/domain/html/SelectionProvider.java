package io.skysail.domain.html;

import java.util.Map;

import org.restlet.resource.Resource;

public interface SelectionProvider {

    public Map<String, String> getSelections();

    public void setConfiguration(Object osgiServicesProvider);

    public void setResource(Resource resource);

}
