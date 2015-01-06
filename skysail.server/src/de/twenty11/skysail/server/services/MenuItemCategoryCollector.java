package de.twenty11.skysail.server.services;

public class MenuItemCategoryCollector extends MenuItem {

	private MenuItem.Category category;

	public MenuItemCategoryCollector(String name, MenuItem.Category category) {
		super(null, name);//, Object.class);
		this.category = category;
	}

	public void collect(MenuItem... item) {
		for (MenuItem menuItem : item) {
			if (category == null && menuItem.getCategory() == null) {
				addMenuItem(menuItem);
			} else if (category != null && category.equals(menuItem.getCategory())) {
				addMenuItem(menuItem);
			}
		}
	}
}
