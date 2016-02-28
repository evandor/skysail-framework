package io.skysail.server.commands.test;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.server.app.ApplicationListProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.commands.ListRoutesCommand;

public class ListRoutesCommandTest {

    private ListRoutesCommand listRoutesCommand;
    private ApplicationListProvider alp;

    @Before
    public void setUp() throws Exception {
        listRoutesCommand = new ListRoutesCommand();
        Field alpField = listRoutesCommand.getClass().getDeclaredField("alp");
        alpField.setAccessible(true);
        alp = Mockito.mock(ApplicationListProvider.class);
        alpField.set(listRoutesCommand, alp);
    }

    @Test
    public void testName() {
        SkysailApplication theApplication = Mockito.mock(SkysailApplication.class);
        Mockito.when(theApplication.getName()).thenReturn("someApp");
        Mockito.when(alp.getApplications()).thenReturn(Arrays.asList(theApplication));
        listRoutesCommand.listRoutes("someApp");
    }
}
