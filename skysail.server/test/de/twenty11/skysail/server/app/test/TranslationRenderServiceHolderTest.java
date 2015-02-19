package de.twenty11.skysail.server.app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.text.TranslationRenderService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Constants;

import de.twenty11.skysail.server.app.TranslationRenderServiceHolder;

public class TranslationRenderServiceHolderTest {

    private TranslationRenderService service;
    private Map<String, String> props;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(TranslationRenderService.class);
        props = new HashMap<>();
    }

    @Test
    public void serviceRanking_is_read_from_properties() {
        props.put(Constants.SERVICE_RANKING, "42");
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        assertThat(holder.getServiceRanking(), is(42));
    }

    @Test
    public void serviceRanking_is_zero_when_not_found_on_properties() {
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        assertThat(holder.getServiceRanking(), is(0));
    }

    @Test
    public void serviceRanking_is_zero_when_not_parsable() {
        props.put(Constants.SERVICE_RANKING, "42a");
        TranslationRenderServiceHolder holder = new TranslationRenderServiceHolder(service, props);
        assertThat(holder.getServiceRanking(), is(0));
    }

}
