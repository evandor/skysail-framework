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
     *   MenuItem menuItem = new MenuItem("Incubator", "/incubator");
     *   menuItem.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
     *   new MenuItem(menuItem, "add new application", "application?media=htmlform");
     *   return Arrays.asList(menuItem);
     * </code>
     * </pre>
     */
    List<MenuItem> getMenuEntries();

}
