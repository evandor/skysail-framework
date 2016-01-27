package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.skysail.server.app.designer.codegen.templates.StPath;

public class StPathTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none(); 
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void null_path_yields_exception() {
        thrown.expect(NullPointerException.class);
        new StPath(null);
    }

    @Test
    public void empty_path_yields_exception() {
        thrown.expect(IllegalArgumentException.class);
        new StPath(" ");
    }

    @Test
    public void bundleDir_is_set_to_code_for_simple_path() {
        StPath stPath = new StPath("template.stg");
        assertThat(stPath.getBundleDir(),is("/code"));
    }

    @Test
    public void templateName_is_set_to_code_for_simple_path() {
        StPath stPath = new StPath("template.stg");
        assertThat(stPath.getTemplateName(),is("template"));
    }

    @Test
    public void bundleDir_is_set_to_code_for_path_with_slashes() {
        StPath stPath = new StPath("OSGI-INF/template.st");
        assertThat(stPath.getBundleDir(),is("/code/OSGI-INF"));
    }

    @Test
    public void templateName_is_set_to_code_for_path_with_slashes() {
        StPath stPath = new StPath("OSGI-INF/template.st");
        assertThat(stPath.getTemplateName(),is("template"));
    }

}
