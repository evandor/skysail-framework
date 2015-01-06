package de.twenty11.skysail.server.resources;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.services.MenuItem;

/**
 * Default resource, attached to path "/".
 * 
 */
public class DefaultResource extends ListServerResource<String> {

    private static Logger logger = LoggerFactory.getLogger(DefaultResource.class);

    public DefaultResource() {
        super(null);
        logger.debug("instanciation of DefaultResource");
    }

    @Override
    public List<String> getData() {
        return Collections.emptyList();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        SkysailRootApplication defaultApp = (SkysailRootApplication) getApplication();
        Set<MenuItem> menuItems = defaultApp.getMenuItems();
        List<Linkheader> linkheaders = menuItems.stream().map(mi -> createLinkheaderForApp(mi))
                .sorted((l1, l2) -> l1.getTitle().compareTo(l2.getTitle())).collect(Collectors.toList());
        if (SecurityUtils.getSubject().isAuthenticated()) {
            linkheaders.add(new Linkheader.Builder(SkysailRootApplication.LOGOUT_PATH + "?targetUri=/")
                    .relation(LinkHeaderRelation.CREATE_FORM).title("Logout").build());
        } else {
            linkheaders.add(new Linkheader.Builder(SkysailRootApplication.LOGIN_PATH)
                    .relation(LinkHeaderRelation.CREATE_FORM).title("Login form").authenticationNeeded(false).build());
        }
        linkheaders.add(new Linkheader.Builder("/usermanagement/registrations/")
                .relation(LinkHeaderRelation.CREATE_FORM).title("Register new User").authenticationNeeded(false).build());
        return linkheaders;
    }

    private Linkheader createLinkheaderForApp(MenuItem mi) {
        Predicate<String[]> securedBy = null;
        return new Linkheader.Builder(mi.getLink()).relation(LinkHeaderRelation.ITEM).title(mi.getName())
                .authenticationNeeded(true).needsRoles(securedBy).build().setShowInHtml(false);
    }

    @Override
    public String redirectTo() {
        return ((SkysailRootApplication) getApplication()).getRedirectTo();
        // "/_iframe?url=http://evandor.gitbooks.io/skysail/content/about.html";
    }

}
