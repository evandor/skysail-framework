package io.skysail.server.converter.wrapper;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.security.Role;

import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItem.Category;
import de.twenty11.skysail.server.services.MenuItemProvider;
import de.twenty11.skysail.server.services.SearchService;

public class STServicesWrapper {

    private Set<MenuItemProvider> menuProviders;
    private SkysailServerResource<?> resource;
    private SearchService searchService;

    public STServicesWrapper(Set<MenuItemProvider> menuProviders, SearchService searchService,
            SkysailServerResource<?> resource) {
        this.menuProviders = menuProviders;
        this.searchService = searchService;
        this.resource = resource;
    }

    public Set<MenuItem> getMainMenuItems() throws Exception {
        return getMenuItems(MenuItem.Category.APPLICATION_MAIN_MENU);
    }

    public Set<MenuItem> getFrontendMenuItems() throws Exception {
        return getMenuItems(MenuItem.Category.FRONTENDS_MAIN_MENU);
    }

    public Set<MenuItem> getDesignerAppMenuItems() throws Exception {
        return getMenuItems(MenuItem.Category.DESIGNER_APP_MENU);
    }

    public Set<MenuItem> getAdminMenuItems() throws Exception {
        Set<MenuItem> menuItems = getMenuItems(MenuItem.Category.ADMIN_MENU);
        menuItems.addAll(getMenuItems(Category.ADMIN_MAIN_MENU_INTERACTIVITY));
        return menuItems;
    }

    public Set<MenuItem> getDesignerAppItems() throws Exception {
        return getMenuItems(MenuItem.Category.DESIGNER_APP_MENU);
    } 
    
    public SearchService getSearchService() {
        return searchService;
    }

    private Set<MenuItem> getMenuItems(Category category) {
        return menuProviders.stream() //
                .map(provider -> provider.getMenuEntries()) //
                .filter(menuEntries -> (menuEntries != null)) //
                .flatMap(entries -> entries.stream()) //
                .collect(Collectors.toSet()) //
                .stream() //
                .filter(item -> (item.getCategory().equals(category))) //
                .sorted((a, b) -> {
                    return b.getName().compareTo(a.getName());
                }).filter(item -> isAuthorized(item)).collect(Collectors.toSet());
    }

    private boolean isAuthorized(MenuItem item) {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        List<Role> clientRoles = resource.getRequest().getClientInfo().getRoles();
        if (!item.getNeedsAuthentication()) {
            return true;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (item.getSecuredByRole() != null) {
            if (item.getSecuredByRole().apply(clienRoleNames.toArray(new String[clienRoleNames.size()]))) {
                return true;
            }
        } else {
            if (authenticated) {
                return true;
            }
        }
        return false;
    }

}
