package de.twenty11.skysail.server.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface MenuItemProvider {

    /**
     * Example implementation
     *
     * <pre>
     * <code>
     *   MenuItem menuItem = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
     *   menuItem.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
     *   return Arrays.asList(menuItem);
     * </code>
     * </pre>
     */
    List<MenuItem> getMenuEntries();

}
