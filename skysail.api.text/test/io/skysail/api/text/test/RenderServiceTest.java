package io.skysail.api.text.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.text.Store;
import io.skysail.api.text.Translation;

import java.util.Locale;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.restlet.Request;

public class RenderServiceTest {

    private LowerCaseRenderService renderService;

    @Before
    public void setUp() throws Exception {
        renderService = new LowerCaseRenderService();
    }

    @Test
    public void returns_key_if_nothing_found() {
        Translation translation = renderService.getTranslation("key", null, null, null);
        assertThat(translation.getTranslation(), is(equalTo("key")));
    }

    @Test
    public void delegates_to_store_if_set() {
        Store store = new Store() {
            @Override
            public Optional<String> get(String key) {
                return Optional.of(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl) {
                return Optional.of(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl, Request request) {
                return Optional.of(key);
            }

            @Override
            public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
                return Optional.of(key);
            }
        };
        Translation translation = renderService.getTranslation("KEY", null, null, store);
        assertThat(translation.getTranslation(), is(equalTo("KEY")));
        assertThat(renderService.render(translation), is(equalTo("key")));
    }
}
