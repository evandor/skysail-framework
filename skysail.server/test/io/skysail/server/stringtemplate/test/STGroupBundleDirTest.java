package io.skysail.server.stringtemplate.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.CompiledST;

import io.skysail.server.stringtemplate.STGroupBundleDir;

public class STGroupBundleDirTest {

    private STGroupBundleDir groupBundleDir;

    @Before
    public void setUp() throws Exception {
        Bundle bundle = Mockito.mock(Bundle.class);
        Mockito.when(bundle.getSymbolicName()).thenReturn("symbolicName");
        Mockito.when(bundle.getVersion()).thenReturn(new Version("1.0.0"));
        URL currentDirUrl = new File("test").toURI().toURL(); // using file protocol in tests instead of "bundle"
        Mockito.when(bundle.getResource("resourcePath")).thenReturn(currentDirUrl);
        
        groupBundleDir = new STGroupBundleDir(bundle, new ATestResource(), "resourcePath");
    }

    @Test
    public void testSTGroupBundleDir() throws Exception {
        assertThat(groupBundleDir.toString(), containsString("symbolicName"));
        assertThat(groupBundleDir.toString(), containsString("resourcePath"));
    }

    @Test
    public void unknown_InstanceOf_yields_null_value() throws Exception {
        ST instanceOf = groupBundleDir.getInstanceOf("unknown");
        assertThat(instanceOf, is(nullValue()));
    }
    
    @Test
    public void load_fails_silently_if_template_file_doesNotExist() {
        CompiledST load = groupBundleDir.load("/notThere");
        assertThat(load, is(nullValue()));        
    }

    @Test
    public void load_finds_resource_level_stringTemplateFile() {
        CompiledST load = groupBundleDir.load("/renderTable");
        assertThat(load, is(not(nullValue())));
    }

    @Test
    public void loadTemplateFile_fails_silently_if_template_file_doesNotExist() {
        CompiledST load = groupBundleDir.loadTemplateFile("/", "notThere.st");
        assertThat(load, is(nullValue()));
    }
    
    @Test
    public void loadTemplateFile_finds_top_level_stringTemplateFile() {
        CompiledST load = groupBundleDir.loadTemplateFile("/", "index.st");
        assertThat(load, is(not(nullValue())));
    }
    
    

}
