package io.skysail.api.responses.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationDetails;
import io.skysail.api.responses.ConstraintViolationsResponse;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.data.Reference;

public class ConstraintViolationsResponseTest {

    @Test
    public void testName() {
        Reference reference = Mockito.mock(Reference.class);
        Set<ConstraintViolation<String>> constrainViolations = new HashSet<>();
        @SuppressWarnings("unchecked")
        ConstraintViolation<String> violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getMessage()).thenReturn ("theMessage");
        Path thePath = Mockito.mock(Path.class);
        Mockito.when(thePath.toString()).thenReturn("thePath");
        Mockito.when(violation.getPropertyPath()).thenReturn(thePath);
        constrainViolations.add(violation);
        
        ConstraintViolationsResponse<String> response = new ConstraintViolationsResponse<String>(reference, "entity",
                constrainViolations);
        assertThat(response.getViolations().size(), is(1));
        ConstraintViolationDetails next = response.getViolations().iterator().next();
        assertThat(next.getMessage(),is(equalTo("theMessage")));
        assertThat(next.getPropertyPath(),is(equalTo("thePath")));
    }

}
