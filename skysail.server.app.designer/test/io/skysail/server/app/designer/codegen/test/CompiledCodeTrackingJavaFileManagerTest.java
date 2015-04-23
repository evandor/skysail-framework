package io.skysail.server.app.designer.codegen.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import io.skysail.server.app.designer.codegen.*;

import java.io.IOException;
import java.util.*;

import javax.tools.*;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

import org.junit.*;

public class CompiledCodeTrackingJavaFileManagerTest {

    private Map<String, CompiledCode> compiledCodes;
    private CompiledCode compiledCode;

    @Before
    public void setUp() throws Exception {
        compiledCodes = new HashMap<>();
        compiledCode = new CompiledCode("className");
    }

    @Test
    public void added_compiledCode_can_be_retrieved_by_its_classname() throws IOException {
        CompiledCodeTrackingJavaFileManager fileManager = new CompiledCodeTrackingJavaFileManager(compiledCodes);
        fileManager.setClassLoader(new DynamicClassLoader(null));
        fileManager.add(compiledCode);
        FileObject sibling = mock(FileObject.class);
        Location location = mock(Location.class);
        assertThat(fileManager.getJavaFileForOutput(location, "className", Kind.CLASS, sibling), is(compiledCode));
        fileManager.close();
    }
}
