package io.skysail.server.testsupport;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;

import org.restlet.data.Status;

public class PostResourceTest extends ResourceTestBase {

    protected void assertValidationFailure(ConstraintViolationsResponse<?> post, String path, String msg) {
        assertThat(response.getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
        assertThat((post).getViolations().size(),is(1));
        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>)post).getViolations().iterator().next();
        assertThat(violation.getPropertyPath(),is(containsString(path)));
        assertThat(violation.getMessage(),is(containsString(msg)));
    }

}
