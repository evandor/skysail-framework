package de.twenty11.skysail.api.services.logentry.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

import de.twenty11.skysail.api.services.logentry.BundleEventLogEntry;

public class BundleEventLogEntryTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private BundleEvent event;

    private Bundle theBundle;

    private Bundle theOrigin;

    @Before
    public void setUp() throws Exception {
        event = Mockito.mock(BundleEvent.class);
        theBundle = Mockito.mock(Bundle.class);
        theOrigin = Mockito.mock(Bundle.class);
        Mockito.when(event.getBundle()).thenReturn(theBundle);
        Mockito.when(event.getOrigin()).thenReturn(theOrigin);
    }

    @Test
    public void calling_constructor_with_null_throws_exception() throws Exception {
        thrown.expect(NullPointerException.class);
        new BundleEventLogEntry(null);
    }
    
    @Test
    public void captures_stopped_bundle() throws Exception {
        Mockito.when(event.getType()).thenReturn(4);
        BundleEventLogEntry bundleEventLogEntry = new BundleEventLogEntry(event);
        assertThat(bundleEventLogEntry.toString().trim(), containsString("stopped"));
    }
    
    @Test
    public void captures_installed_bundle() throws Exception {
        Mockito.when(event.getType()).thenReturn(1);
        BundleEventLogEntry bundleEventLogEntry = new BundleEventLogEntry(event);
        assertThat(bundleEventLogEntry.toString().trim(), containsString("installed"));
    }
    
    @Test
    public void captures_resolved_bundle() throws Exception {
        Mockito.when(event.getType()).thenReturn(32);
        BundleEventLogEntry bundleEventLogEntry = new BundleEventLogEntry(event);
        assertThat(bundleEventLogEntry.toString().trim(), containsString("resolved"));
    }

    @Test
    public void captures_uninstalled_bundle() throws Exception {
        Mockito.when(event.getType()).thenReturn(16);
        BundleEventLogEntry bundleEventLogEntry = new BundleEventLogEntry(event);
        assertThat(bundleEventLogEntry.toString().trim(), containsString("uninstalled"));
    }

}
