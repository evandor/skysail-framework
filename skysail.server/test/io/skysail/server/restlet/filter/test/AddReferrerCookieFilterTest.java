package io.skysail.server.restlet.filter.test;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.filter.AddReferrerCookieFilter;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.*;
import org.restlet.util.Series;

@RunWith(MockitoJUnitRunner.class)
public class AddReferrerCookieFilterTest {

    @Mock
    private EntityServerResource<AIdentifiable> resource;

    @Mock
    private Response response;

    @InjectMocks
    private AddReferrerCookieFilter<EntityServerResource<AIdentifiable>, AIdentifiable> addReferrerCookieFilter;

    @Mock
    private Reference referrerRef;

    public class AIdentifiable implements Identifiable {

        @Override
        public String getId() {
            return "id";
        }

        @Override
        public void setId(String id) {
        }

    }


    @Before
    public void setUp()  {
        addReferrerCookieFilter = new AddReferrerCookieFilter<>();
        Mockito.when(resource.getReferrerRef()).thenReturn(referrerRef);
    }

    @Test
    public void testName() {
        Mockito.when(referrerRef.getPath()).thenReturn("/path");
        @SuppressWarnings("unchecked")
        Series<CookieSetting> cookieSettings = Mockito.mock(Series.class);
        Mockito.when(response.getCookieSettings()).thenReturn(cookieSettings);
        addReferrerCookieFilter.handle(resource, response);
        Mockito.verify(cookieSettings).add(Mockito.argThat(new ArgumentMatcher<CookieSetting>() {
            @Override
            public boolean matches(Object argument) {
                if (argument instanceof CookieSetting) {
                    CookieSetting settings = (CookieSetting)argument;
                    if (
                        settings.getValue().equals("/path") &&
                        settings.getPath().equals("/")) {
                            return true;
                    }
                }
                return false;
            }}));
    }

}
