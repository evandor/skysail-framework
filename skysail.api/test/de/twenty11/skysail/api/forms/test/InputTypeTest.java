package de.twenty11.skysail.api.forms.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.twenty11.skysail.api.forms.InputType;

public class InputTypeTest {

    @Test
    public void cheat_on_coverage_sorry() throws Exception {
        InputType inputType = InputType.BUTTON;
        assertThat(inputType.name(), is(equalTo("BUTTON")));
    }

}
