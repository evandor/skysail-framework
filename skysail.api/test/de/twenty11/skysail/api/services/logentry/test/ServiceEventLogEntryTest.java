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
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import de.twenty11.skysail.api.services.logentry.ServiceEventLogEntry;

public class ServiceEventLogEntryTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ServiceEvent event;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        event = Mockito.mock(ServiceEvent.class);
        @SuppressWarnings("rawtypes")
        ServiceReference sr = Mockito.mock(ServiceReference.class);
        Bundle theBundle = Mockito.mock(Bundle.class);
        Mockito.when(sr.getBundle()).thenReturn(theBundle);
        Mockito.when(event.getServiceReference()).thenReturn(sr);
    }

    @Test
    public void calling_constructor_with_null_throws_exception() throws Exception {
        thrown.expect(NullPointerException.class);
        new ServiceEventLogEntry(null);
    }

    @Test
    public void captures_modified_service() throws Exception {
        Mockito.when(event.getType()).thenReturn(1);
        ServiceEventLogEntry eventLogEntry = new ServiceEventLogEntry(event);
        assertThat(eventLogEntry.toString().trim(), containsString("modified"));
    }
}
