package de.twenty11.skysail.server.core.restlet.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.core.restlet.utils.StringParserUtils;

public class StringParserUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    private Resource entityResource;
    private ConcurrentMap<String, Object> attributes;

    @Before
    public void setUp() throws Exception {
        attributes = new ConcurrentHashMap<String, Object>();
        entityResource = Mockito.mock(Resource.class);
        Request request = Mockito.mock(Request.class);
        Mockito.when(entityResource.getRequest()).thenReturn(request);
        Mockito.when(request.getAttributes()).thenReturn(attributes);
    }

    @Test
    public void ignores_null_input() throws Exception {
        String result = StringParserUtils.substitutePlaceholders(null, entityResource);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void leaves_string_without_placeholders_untouched() throws Exception {
        String result = StringParserUtils.substitutePlaceholders("teststring", entityResource);
        assertThat(result, is(equalTo("teststring")));
    }

    @Test
    public void replaces_one_placeholder() throws Exception {
        attributes.put("id", "1234");
        String result = StringParserUtils.substitutePlaceholders("/path/with/{id}", entityResource);
        assertThat(result, is(equalTo("/path/with/1234")));
    }

    @Test
    public void replaces_two_placeholders() throws Exception {
        attributes.put("idA", "A");
        attributes.put("idB", "B");
        String result = StringParserUtils.substitutePlaceholders("/path/{idA}/with/{idB}", entityResource);
        assertThat(result, is(equalTo("/path/A/with/B")));
    }
}
