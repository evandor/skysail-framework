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
     *    public List&lt;MenuItem&gt; getMenuEntries() {
     *         MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
     *         appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
     *         return Arrays.asList(appMenu);
     *    }
     * </code>
     * </pre>
     */
    List<MenuItem> getMenuEntries();

}
