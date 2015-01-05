package de.twenty11.skysail.api.services.logentry.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkEvent;

import de.twenty11.skysail.api.services.logentry.FrameworkEventLogEntry;

public class FrameworkEventLogEntryTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private FrameworkEvent event;

    private Bundle theBundle;

    @Before
    public void setUp() throws Exception {
        event = Mockito.mock(FrameworkEvent.class);
        theBundle = Mockito.mock(Bundle.class);
        Mockito.when(event.getBundle()).thenReturn(theBundle);
    }

    @Test
    public void calling_constructor_with_null_throws_exception() throws Exception {
        thrown.expect(NullPointerException.class);
        new FrameworkEventLogEntry(null);
    }
    
    @Test
    public void captures_packages_refreshed_event() throws Exception {
        Mockito.when(event.getType()).thenReturn(4);
        FrameworkEventLogEntry eventLogEntry = new FrameworkEventLogEntry(event);
        assertThat(eventLogEntry.toString().trim(), containsString("PACKAGES_REFRESHED"));
    }
    
    @Test
    public void captures_started_event() throws Exception {
        Mockito.when(event.getType()).thenReturn(1);
        FrameworkEventLogEntry eventLogEntry = new FrameworkEventLogEntry(event);
        assertThat(eventLogEntry.toString().trim(), containsString("STARTED"));
    }
    
    @Test
    public void captures_info_event() throws Exception {
        Mockito.when(event.getType()).thenReturn(32);
        FrameworkEventLogEntry eventLogEntry = new FrameworkEventLogEntry(event);
        assertThat(eventLogEntry.toString().trim(), containsString("INFO"));
    }

    @Test
    public void captures_warning_event() throws Exception {
        Mockito.when(event.getType()).thenReturn(16);
        FrameworkEventLogEntry eventLogEntry = new FrameworkEventLogEntry(event);
        assertThat(eventLogEntry.toString().trim(), containsString("WARNING"));
    }
}
