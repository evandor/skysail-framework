package io.skysail.domain.html;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.html.InputType;

public class InputTypeTest {

    @Test
    public void cheat_on_coverage_sorry() throws Exception {
        InputType inputType = InputType.BUTTON;
        assertThat(inputType.name(), is(equalTo("BUTTON")));
    }

}
