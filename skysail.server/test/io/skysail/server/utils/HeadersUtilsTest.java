package io.skysail.server.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.utils.HeadersUtils;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HeadersUtilsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void creates_empty_languageList_for_null_input() {
        assertThat(HeadersUtils.parseAcceptedLanguages(null).size(), is(0));
    }

    @Test
    public void creates_empty_languageList_for_empty_input() {
        assertThat(HeadersUtils.parseAcceptedLanguages(" ").size(), is(0));
    }

    @Test
    public void creates_list_with_one_langue_for_simple_input() {
        assertThat(HeadersUtils.parseAcceptedLanguages(" de ").size(), is(1));
        assertThat(HeadersUtils.parseAcceptedLanguages(" de ").get(0), is(equalTo("de")));
    }

    @Test
    public void creates_list_with_one_langue_for_simple_weighted_input() {
        List<String> languages = HeadersUtils.parseAcceptedLanguages("en;q=0.5");
        assertThat(languages.size(), is(1));
        assertThat(languages.get(0), is(equalTo("en")));
    }

    @Test
    public void creates_sorted_list_for_weighted_input() {
        List<String> languages = HeadersUtils.parseAcceptedLanguages("en-gb;q=0.8, en;q=0.7, de");
        assertThat(languages.size(), is(3));
        assertThat(languages.get(0), is(equalTo("de")));
        assertThat(languages.get(1), is(equalTo("en-gb")));
        assertThat(languages.get(2), is(equalTo("en")));
    }

}
