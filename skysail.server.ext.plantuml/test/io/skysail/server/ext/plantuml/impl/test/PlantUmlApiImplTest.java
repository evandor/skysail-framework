package io.skysail.server.ext.plantuml.impl.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.ext.plantuml.impl.PlantUmlApiImpl;

import java.io.IOException;

import org.junit.*;

@Ignore
public class PlantUmlApiImplTest {

    private PlantUmlApiImpl plantUmlApiImpl;

    @Before
    public void setUp() throws Exception {
        plantUmlApiImpl = new PlantUmlApiImpl();
    }

    @Test
    public void png_is_created_from_valid_source() throws IOException {
        plantUmlApiImpl.createPng("Bob -> Alice : hello\n", "generated/testfile.png");
    }

    @Test
    public void returns_svg_from_valid_source() throws IOException {
        String svg = plantUmlApiImpl.getSvg("Bob -> Alice : hello\n");
        assertThat(svg, containsString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><svg"));
        System.out.println(svg);
    }

}
