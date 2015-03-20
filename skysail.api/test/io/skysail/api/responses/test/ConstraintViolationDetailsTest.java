package io.skysail.api.responses.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationDetails;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConstraintViolationDetailsTest {

    @Before
    public void setUp() {

    }

    @Test(expected = IllegalArgumentException.class)
    public void throws_exception_when_null_is_passed() {
        new ConstraintViolationDetails(null);
    }

    @Test
    public void extracts_data_from_property_path_and_message() {
        ConstraintViolation<?> noViolations = Mockito.mock(ConstraintViolation.class);
        Path path = Mockito.mock(Path.class);
        Mockito.when(path.toString()).thenReturn("path");
        Mockito.when(noViolations.getPropertyPath()).thenReturn(path);
        Mockito.when(noViolations.getMessage()).thenReturn("msg");
        ConstraintViolationDetails cvd = new ConstraintViolationDetails(noViolations);

        assertThat(cvd.getMessage(), is(equalTo("msg")));
        assertThat(cvd.getPropertyPath(), is(equalTo("path")));
    }

}
