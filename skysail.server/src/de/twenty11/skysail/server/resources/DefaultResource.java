package de.twenty11.skysail.server.resources;

import io.skysail.api.links.*;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.services.MenuItem;

/**
 * Default resource, attached to path "/".
 * 
 */
public class DefaultResource extends ListServerResource<MenuItemDescriptor> {
    
    @Override
    public List<Link> getLinks() {
        SkysailRootApplication defaultApp = (SkysailRootApplication) getApplication();
        Set<MenuItem> menuItems = defaultApp.getMenuItems();
        List<Link> linkheaders = menuItems.stream().map(mi -> createLinkheaderForApp(mi))
                .sorted((l1, l2) -> l1.getTitle().compareTo(l2.getTitle())).collect(Collectors.toList());
        if (SecurityUtils.getSubject().isAuthenticated()) {
//            linkheaders.add(new Link.Builder(SkysailRootApplication.LOGOUT_PATH + "?targetUri=/")
//                    .relation(LinkRelation.CREATE_FORM).title("Logout").build());
        } else {
            linkheaders.add(new Link.Builder(SkysailRootApplication.LOGIN_PATH)
                    .relation(LinkRelation.CREATE_FORM).title("Login form").authenticationNeeded(false).build());
        }
//        linkheaders.add(new Link.Builder("/usermanagement/registrations/")
//                .relation(LinkRelation.CREATE_FORM).title("Register new User").authenticationNeeded(false)
//                .build());
        return linkheaders;
    }

    private Link createLinkheaderForApp(MenuItem mi) {
        Predicate<String[]> securedBy = null;
        return new Link.Builder(mi.getLink()).relation(LinkRelation.ITEM).title(mi.getName()).role(LinkRole.MENU_ITEM)
                .authenticationNeeded(true).needsRoles(securedBy).build();
    }

    @Override
    public String redirectTo() {
        return ((SkysailRootApplication) getApplication()).getRedirectTo();
        // "/_iframe?url=http://evandor.gitbooks.io/skysail/content/about.html";
    }

    @Override
    public List<MenuItemDescriptor> getEntity() {
        Set<MenuItem> mainMenuItems = ((SkysailRootApplication)getApplication()).getMainMenuItems(getRequest());
        return mainMenuItems.stream().map(i -> new MenuItemDescriptor(i)).collect(Collectors.toList());
    }

}
