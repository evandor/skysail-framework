package io.skysail.server.app.designer.codegen.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.codegen.SourceCode;

import org.junit.Before;
import org.junit.Test;

public class SourceCodeTest {

    private SourceCode sourceCode;

    @Before
    public void setUp() throws Exception {
        sourceCode = new SourceCode("io.skysail.Classname", "code");
    }

    @Test
    public void testName() throws Exception {
        assertThat(sourceCode.getName(), is(equalTo("/io/skysail/Classname.java")));
    }
    
    @Test
    public void testUri() throws Exception {
        assertThat(sourceCode.toUri().toString(), is(equalTo("string:///io/skysail/Classname.java")));
    }

}
