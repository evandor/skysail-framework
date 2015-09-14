package io.skysail.server.queryfilter.pagination.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.queryfilter.pagination.Pagination;

import java.util.concurrent.*;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.*;

public class PaginationTest {

    private Pagination pagination;

    @Before
    public void setUp() throws Exception {
        Request request = Mockito.mock(Request.class);
        Response response = Mockito.mock(Response.class);

        ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<>();
        Mockito.when(response.getAttributes()).thenReturn(attributes);

        Mockito.when(request.getAttributes()).thenReturn(new ConcurrentHashMap<>());
        pagination = new Pagination(request, response, 44);
    }

    @Test
    public void default_limit_clause_starts_at_zero_ends_at_10() {
        String limitClause = pagination.getLimitClause();
        assertThat(limitClause, is("SKIP 0 LIMIT 10"));
    }

    @Test
    public void pagination_starts_at_first_page() {
        assertThat(pagination.getPage(), is(1));
    }

}
