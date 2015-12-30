package io.skysail.domain.html;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.domain.html.AllowedAttribute;

public class AllowedAttributeTest {

    private AllowedAttribute attribute;

    @Before
    public void setUp() throws Exception {
        attribute = new AllowedAttribute("style");
    }

    @Test(expected = NullPointerException.class)
    public void needs_name() throws Exception {
        attribute = new AllowedAttribute(null);
    }

    @Test
    public void sets_name() throws Exception {
        assertThat(attribute.getName(), is(equalTo("style")));
    }

    @Test
    public void sets_elements() throws Exception {
        attribute.onElements(new String[] { "span", "div" });
        assertThat(attribute.getForElements().length, is(2));
    }

}
