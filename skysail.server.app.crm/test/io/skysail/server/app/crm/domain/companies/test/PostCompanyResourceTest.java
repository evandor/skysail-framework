package io.skysail.server.app.crm.domain.companies.test;

import io.skysail.server.app.crm.domain.companies.CompaniesRepository;
import io.skysail.server.app.crm.domain.companies.PostCompanyResource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sound.sampled.Clip;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostCompanyResourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    private PostCompanyResource resource;

    @Mock
    private ClipboardApplication clipboardApplication;

    @Mock
    private CompaniesRepository repo;

    private Clip clip;

    private ConcurrentMap<String, Object> attributes;

    @Before
    public void setUp() throws Exception {
        Mockito.when(clipboardApplication.getClipsRepository()).thenReturn(repo);
        clip = new Clip();
        attributes = new ConcurrentHashMap<String, Object>();
    }

    @Test
    public void creates_registration_template() throws Exception {
        resource.init(null, null, null);
        Clip template = resource.createEntityTemplate();
        assertThat(template.getContent(), is(nullValue()));
    }

    @Test
    @Ignore
    public void creates_clip_with_content_and_creation_date() {
        // Request request = Mockito.mock(Request.class);
        // Mockito.when(request.getClientInfo()).thenReturn(new ClientInfo());
        // Mockito.when(request.getAttributes()).thenReturn(attributes);
        // Reference resourceRef = Mockito.mock(Reference.class);
        // Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        // Response response = new Response(request);
        // // Mockito.when(response.getRequest()).thenReturn(request);
        // resource.init(null, request, response);
        // Clip clipWithId = new Clip();
        // clipWithId.setRid("#12:0");
        // Mockito.when(repo.add(clip)).thenReturn(clipWithId);
        // Form form = Mockito.mock(Form.class);
        // Object posted = resource.post(form);
        // assertThat(response.getStatus().getCode(), is(200));
    }
}
