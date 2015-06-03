//package io.skysail.server.converter.wrapper;
//
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.utils.MenuItemUtils;
//
//import java.util.Set;
//
//import de.twenty11.skysail.server.services.*;
//import de.twenty11.skysail.server.services.MenuItem.Category;
//
//public class STServicesWrapper {
//
//    private Set<MenuItemProvider> menuProviders;
//    private SkysailServerResource<?> resource;
//    private SearchService searchService;
//
//    public STServicesWrapper(Set<MenuItemProvider> menuProviders, SearchService searchService,
//            SkysailServerResource<?> resource) {
//        this.menuProviders = menuProviders;
//        this.searchService = searchService;
//        this.resource = resource;
//    }
//
//    public Set<MenuItem> getMainMenuItems() throws Exception {
//        return MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), MenuItem.Category.APPLICATION_MAIN_MENU);
//    }
//
//    public Set<MenuItem> getFrontendMenuItems() throws Exception {
//        return MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), MenuItem.Category.FRONTENDS_MAIN_MENU);
//    }
//
//    public Set<MenuItem> getDesignerAppMenuItems() throws Exception {
//        return MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), MenuItem.Category.DESIGNER_APP_MENU);
//    }
//
//    public Set<MenuItem> getAdminMenuItems() throws Exception {
//        Set<MenuItem> menuItems = MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), MenuItem.Category.ADMIN_MENU);
//        menuItems.addAll(MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), Category.ADMIN_MAIN_MENU_INTERACTIVITY));
//        return menuItems;
//    }
//
//    public Set<MenuItem> getDesignerAppItems() throws Exception {
//        return MenuItemUtils.getMenuItems(menuProviders, resource.getRequest(), MenuItem.Category.DESIGNER_APP_MENU);
//    } 
//    
//    public SearchService getSearchService() {
//        return searchService;
//    }
//
//    
//
//}
