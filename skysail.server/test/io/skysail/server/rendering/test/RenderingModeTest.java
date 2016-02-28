package io.skysail.server.rendering.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import io.skysail.server.rendering.RenderingMode;

public class RenderingModeTest {

    @Test
    public void testValues() {
        List<String> values = Arrays.stream(RenderingMode.values()).map(v -> v.name()).collect(Collectors.toList());
        assertThat(values,hasItems("DEFAULT", "DEBUG", "EDIT"));
    }
}
