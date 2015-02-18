package io.skysail.server.text.test;

import io.skysail.server.text.ResourceBundleStore;

import java.util.MissingResourceException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ResourceBundleStoreTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ResourceBundleStore store;

    @Before
    public void setUp() throws Exception {
        store = new ResourceBundleStore();
    }

    @Test
    @Ignore
    public void store_cannot_find_resource_without_proper_classloader() throws Exception {
        thrown.expect(MissingResourceException.class);
        store.get("key");
    }

    @Test
    @Ignore
    public void testGetStringClassLoader() throws Exception {
        store.get("key", ResourceBundleStore.class.getClassLoader());
    }

    @Test
    public void testGetStringClassLoaderRequest() throws Exception {

    }

    @Test
    public void testGetStringClassLoaderRequestLocale() throws Exception {

    }

}
