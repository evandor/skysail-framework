package de.twenty11.skysail.server.services.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemCategoryCollector;

public class MenuItemCategoryCollectorTest {

	private MenuItemCategoryCollector categoryCollector;
	private MenuItem itemB;
	private MenuItem itemA;

	@Before
	public void setUp() throws Exception {
		categoryCollector = new MenuItemCategoryCollector("applications", MenuItem.Category.ADMIN_MENU);
		itemA = new MenuItem("A", "linkA");
		itemB = new MenuItem("B", "linkB");
	}

	@Test
	@Ignore
	public void finds_only_menuItems_of_specific_category() {
		itemA.setCategory(MenuItem.Category.ADMIN_MENU);
		itemB.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
		categoryCollector.collect(itemA, itemB);
		assertThat(categoryCollector.getName(),is(equalTo("applications")));
		assertThat(categoryCollector.getChildren().size(),is(1));
	}

}
