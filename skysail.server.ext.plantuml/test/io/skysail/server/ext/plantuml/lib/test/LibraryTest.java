package io.skysail.server.ext.plantuml.lib.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.*;

import net.sourceforge.plantuml.SourceStringReader;

import org.junit.Test;

public class LibraryTest {

    @Test
    public void testName() throws Exception {
        OutputStream png = new FileOutputStream("generated/testfile.png");
        String source = "@startuml\n";
        source += "Bob -> Alice : hello\n";
        source += "@enduml\n";

        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
        assertThat(desc,is(notNullValue()));
    }
}
