package de.twenty11.skysail.server.services;

import io.skysail.server.app.SkysailApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Predicate;

import de.twenty11.skysail.server.core.restlet.ApplicationContextId;

public class MenuItem {

    public enum Category {
        APPLICATION_MAIN_MENU, //
        ADMIN_MENU, //
        ADMIN_MAIN_MENU_INTERACTIVITY, // Section in Admin Main Menu
        FRONTENDS_MAIN_MENU
    }

    private String name;
    private String link;
    private Category category = Category.ADMIN_MENU;
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<>();
    private Predicate<String[]> securedByRole;
    private boolean needsAuthentication = true;
    private boolean openInNewWindow = false;
	private String applicationImage = "";

    public MenuItem(MenuItem parent, String name, String link) {
        this.parent = parent;
        if (parent != null) {
            parent.addMenuItem(this);
        }

        this.name = name;
        this.link = link;
    }

    public MenuItem(String name, String link) {
        this(null, name, link);
    }

//    public MenuItem(SkysailApplication app, Class<? extends SkysailServerResource<?>> skysailServerResourceClass) {
//    	applicationImage  = app.getFromContext(ApplicationContextId.IMG);
//        Link link = LinkUtils.fromResource(app, skysailServerResourceClass);
//        if (link == null) {
//            this.name = app.getName();
//            this.link = app.getName();
//            needsAuthentication = true;
//            return;
//        }
//        this.name = link.getTitle();
//        this.link = link.getUri();
//        securedByRole = link.getRolesPredicate();
//        needsAuthentication = link.getNeedsAuthentication();
//    }

    public MenuItem(String string, String name2, SkysailApplication app) {
    	this(string,name2);
    	applicationImage  = app.getFromContext(ApplicationContextId.IMG);
    }

	public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(MenuItem.Category category) {
        this.category = category;
    }

    protected void addMenuItem(MenuItem menuItem) {
        this.children.add(menuItem);
    }

    public List<MenuItem> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void setOpenInNewWindow(boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }

    public Predicate<String[]> getSecuredByRole() {
        return securedByRole;
    }

    public boolean getNeedsAuthentication() {
        return needsAuthentication;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MenuItem other = (MenuItem) obj;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder(name).append(" [href: ").append(link).append("]").append(" [cat: ").append(category)
                .append("]").toString();
    }

    public boolean isAdminMainMenuInteractivity () {
        return category.equals(Category.ADMIN_MAIN_MENU_INTERACTIVITY);
    }
    
    public String getApplicationImage() {
	    return applicationImage;
    }
}
