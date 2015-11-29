package io.skysail.server.menus;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

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
