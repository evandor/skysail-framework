package io.skysail.api.text.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.text.*;

import java.util.*;

import org.junit.*;
import org.osgi.framework.BundleContext;
import org.restlet.Request;

public class TranslationRenderServiceTest {

    private LowerCaseRenderService renderService;

    @Before
    public void setUp() throws Exception {
        renderService = new LowerCaseRenderService();
    }

    @Test
    public void returns_key_if_nothing_found() {
        Translation translation = renderService.getTranslation("key", null, null, null);
        assertThat(translation.getValue(), is(equalTo("key")));
    }

    @Test
    public void delegates_to_store_if_set() {
        TranslationStore store = new TranslationStore() {
            @Override
            public Optional<String> get(String key) {
                return Optional.ofNullable(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl) {
                return Optional.ofNullable(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl, Request request) {
                return Optional.ofNullable(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
                return Optional.ofNullable(key);
            }

            @Override
            public boolean persist(String key, String message, Locale locale, BundleContext bundleContext) {
                return false;
            }
        };
        Translation translation = renderService.getTranslation("KEY", null, null, store);
        assertThat(translation.getValue(), is(equalTo("KEY")));
        assertThat(renderService.render(translation), is(equalTo("key")));
    }
}
