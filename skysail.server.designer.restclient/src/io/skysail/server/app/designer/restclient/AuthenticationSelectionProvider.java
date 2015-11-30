package io.skysail.server.app.designer.restclient;

import java.util.*;

import org.restlet.resource.Resource;

import io.skysail.api.forms.SelectionProvider;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class AuthenticationSelectionProvider implements SelectionProvider {

    private SkysailServerResource<?> resource;

    public static AuthenticationSelectionProvider getInstance() {
        return new AuthenticationSelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("", "");
        RestclientApplication app = (RestclientApplication) resource.getApplication();
        OAuth2Repo repository = (OAuth2Repo) app.getRepository(OAuth2.class);
        List<OAuth2> authenticationTypes = repository.find(new Filter());
        authenticationTypes.stream().forEach(c -> {
            result.put(c.getId().toString(), c.getName() + " (OAuth2)");
            });
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = (SkysailServerResource<?>)resource;
    }

}
