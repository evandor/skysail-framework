package de.twenty11.skysail.server.core.restlet.converter.internal;

import io.skysail.server.utils.CookiesUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.Resource;
import org.restlet.security.Role;

import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemCategoryCollector;

public class RenderUtils {

    private List<?> skysailResponse;

    public RenderUtils(List<?> skysailResponse) {
        this.skysailResponse = skysailResponse;
    }

    public static CharSequence getMainNav(Set<MenuItem> menuItems, Resource resource) {
        MenuItemCategoryCollector applicationsMenu = new MenuItemCategoryCollector("applications",
                MenuItem.Category.APPLICATION_MAIN_MENU);
        applicationsMenu.collect(menuItems.toArray(new MenuItem[menuItems.size()]));

        StringBuilder sb = new StringBuilder();
        sb.append("<ul class='nav'>\n");

        sb.append(createApplicationsMenu(resource, applicationsMenu));
        sb.append(createAdminMenu(resource, menuItems));
        createViewMenu(resource, sb);

        sb.append("</ul>\n");
        return sb.toString();
    }

    private static String createApplicationsMenu(Resource resource, MenuItemCategoryCollector applicationsMenu) {
        String menuHtml = getMenuHtml(resource, applicationsMenu);
        if (menuHtml.trim().length() == 0) {
            return "";
        }
        return createHtmlList(menuHtml, "Applications");
    }
    
    private static String createAdminMenu(Resource resource, Set<MenuItem> menuItems) {
        MenuItemCategoryCollector adminMenu = new MenuItemCategoryCollector("admin", MenuItem.Category.ADMIN_MENU);
        adminMenu.collect(menuItems.toArray(new MenuItem[menuItems.size()]));

        String menuHtml = getAdminMenuHtml(resource, adminMenu);
        if (menuHtml.trim().length() == 0) {
            return "";
        }
        return createHtmlList(menuHtml, "Admin");
    }

    private static void createViewMenu(Resource resource, StringBuilder sb) {
        String templateFromCookie = CookiesUtils.getTemplateFromCookie(resource.getRequest());
        String templateName = templateFromCookie == null ? "advanced" : "simple";
        sb.append("  <li class='dropdown'>\n");
        sb.append("    <a class='dropdown-toggle' data-toggle='dropdown' href='#'>View (" + templateName
                + ") <b class='caret'></b></a>\n");
        sb.append("    <ul class='dropdown-menu'>\n");
        if (templateFromCookie == null) {
            sb.append("    <li><a href='")
                    .append("javascript:document.cookie=\"template=simple.template\";window.location.reload(true)")
                    .append("'>").append("Simple</a></li>");
        } else {
            sb.append("    <li><a href='")
                    .append("javascript:document.cookie=\"template=; expires=Thu, 01 Jan 1970 00:00:00 GMT\";window.location.reload(true)")
                    .append("'>").append("Advanced</a></li>");
        }
        sb.append("    </ul>\n");
        sb.append("  </li>\n");
    }

    private static String getMenuHtml(Resource resource, MenuItemCategoryCollector applicationsMenu) {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        List<Role> clientRoles = resource.getRequest().getClientInfo().getRoles();
        StringBuilder sb = new StringBuilder();
        for (MenuItem menuItem : applicationsMenu.getChildren()) {
            addMenuItemIfAuthorized(authenticated, clientRoles, sb, menuItem);
        }
        return sb.toString();
    }

    private static void addMenuItemIfAuthorized(boolean authenticated, List<Role> clientRoles, StringBuilder sb,
            MenuItem menuItem) {
        if (menuItem.getNeedsAuthentication() && !authenticated) {
            return;
        }
        List<String> clienRoleNames = clientRoles.stream().map(cr -> cr.getName()).collect(Collectors.toList());
        if (menuItem.getSecuredByRole() != null) {
            if (!(menuItem.getSecuredByRole().apply(clienRoleNames.toArray(new String[clienRoleNames.size()])))) {
                return;
            }
        }
        if (menuItem.isOpenInNewWindow()) {
            sb.append("    <li><a href='#' onClick='javascript:window.open(\"").append(menuItem.getLink())
                    .append("\");'>").append(menuItem.getName()).append("</a></li>");
        } else {
            sb.append("    <li><a href='").append(menuItem.getLink()).append("'>").append(menuItem.getName())
                    .append("</a></li>");
        }
    }

    private static String getAdminMenuHtml(Resource resource, MenuItem adminMenu) {
        boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
        List<Role> clientRoles = resource.getRequest().getClientInfo().getRoles();
        StringBuilder sb = new StringBuilder();
        for (MenuItem menuItem : adminMenu.getChildren()) {
            addMenuItemIfAuthorized(authenticated, clientRoles, sb, menuItem);
        }
        return sb.toString();
    }
    public CharSequence getJsTree(Object data, Resource resource) {

        return null;

    }

    private static String createHtmlList(String menuHtml, String title) {
        StringBuilder sb = new StringBuilder();
        sb.append("  <li class='dropdown'>\n");
        sb.append("    <a class='dropdown-toggle' data-toggle='dropdown' href='#'>").append(title).append("<b class='caret'></b></a>\n");
        sb.append("    <ul class='dropdown-menu'>\n");
        sb.append(menuHtml);
        sb.append("    </ul>\n");
        sb.append("  </li>\n");
        return sb.toString();
    }

}
