package io.skysail.api.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.validation.DefaultValidationImpl;

import javax.validation.Validator;

import org.junit.Test;

public class DefaultValidationImplTest {

    @Test
    public void testName() {
        DefaultValidationImpl defaultValidationImpl = new DefaultValidationImpl();
        Validator validator = defaultValidationImpl.getValidator();
        assertThat(validator, is(notNullValue()));
    }
}
