package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.validation.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.*;
import org.restlet.data.Reference;

import io.skysail.api.responses.*;

public class ConstraintViolationsResponseTest {

    @Test
    public void testName() {
        Response response = Mockito.mock(Response.class);
        Request request = Mockito.mock(Request.class);
        Mockito.when(response.getRequest()).thenReturn(request);
        Reference reference = Mockito.mock(Reference.class);
        Mockito.when(request.getOriginalRef()).thenReturn(reference);
        Set<ConstraintViolation<String>> constrainViolations = new HashSet<>();
        @SuppressWarnings("unchecked")
        ConstraintViolation<String> violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getMessage()).thenReturn ("theMessage");
        Path thePath = Mockito.mock(Path.class);
        Mockito.when(thePath.toString()).thenReturn("thePath");
        Mockito.when(violation.getPropertyPath()).thenReturn(thePath);
        constrainViolations.add(violation);
        
        ConstraintViolationsResponse<String> cvr = new ConstraintViolationsResponse<String>(response, "entity",
                constrainViolations);
        assertThat(cvr.getViolations().size(), is(1));
        ConstraintViolationDetails next = cvr.getViolations().iterator().next();
        assertThat(next.getMessage(),is(equalTo("theMessage")));
        assertThat(next.getPropertyPath(),is(equalTo("thePath")));
    }

}
