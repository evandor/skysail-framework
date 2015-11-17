package io.skysail.server.app.designer.restclient;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

import java.util.*;

import org.restlet.Restlet;
import org.restlet.ext.oauth.OAuthProxy;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class RestclientApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Restclient";

    public RestclientApplication() {
        super(APP_NAME, new ApiVersion(1), getIdentifiables(ClientApplication.class, OAuth2.class));
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    private static List<Class<? extends Identifiable>> getIdentifiables(Class<? extends Identifiable>... classes) {
        List<Class<? extends Identifiable>> result = new ArrayList<>();
        Arrays.stream(classes).forEach(cls -> result.add(cls));
        return result;
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public void setRepositories(Repositories repos) {
       super.setRepositories(repos);
    }

    public void unsetRepositories(DbRepository repo) {
        super.setRepositories(null);
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/ClientApplication/{id}/execution", ExecutionResource.class));
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        Restlet inboundRoot = super.createInboundRoot();

        OAuthProxy oAuthProxy = new OAuthProxy(getContext());
        oAuthProxy.setClientId("1481445908830165");
        oAuthProxy.setClientSecret("d2991f2ae851ef74d65134e596574f88");
        oAuthProxy.setRedirectURI("http://127.0.0.1:2015/Restclient/facebook");
        oAuthProxy.setAuthorizationURI("https://www.facebook.com/dialog/oauth");
        oAuthProxy.setTokenURI("https://graph.facebook.com/oauth/access_token");
        oAuthProxy.setNext(FacebookMeServerResource.class);
        router.attach("/facebook", oAuthProxy);
        return inboundRoot;
    }

}