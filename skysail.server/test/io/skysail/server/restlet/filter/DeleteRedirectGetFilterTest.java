package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.filter.DeleteRedirectGetFilter;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class DeleteRedirectGetFilterTest {
	
	private DeleteRedirectGetFilter<EntityServerResource<String>,String> filter;
	private Request request;
	private EntityServerResource<String> resource;
	private Response response;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		filter = new DeleteRedirectGetFilter<>();
		resource = Mockito.mock(EntityServerResource.class);
		request = Mockito.mock(Request.class);
		response = new Response(request);
	}
	
	@Test
	public void does_not_redirect_by_default()  {
		filter.handle(resource, response);
		assertThat(response.getStatus().getCode(),is(200));
		assertThat(response.getLocationRef(), is(nullValue()));
	}

	@Test
	public void does_redirect_is_asked_to()  {
		Mockito.when(resource.redirectTo()).thenReturn("http://www.someurl.de");
		filter.handle(resource, response);
		assertThat(response.getStatus(),is(equalTo(Status.REDIRECTION_SEE_OTHER)));
		assertThat(response.getLocationRef().toString(), is(equalTo("http://www.someurl.de")));
	}

}
