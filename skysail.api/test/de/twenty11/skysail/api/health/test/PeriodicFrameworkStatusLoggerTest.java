package de.twenty11.skysail.api.health.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import de.twenty11.skysail.api.health.PeriodicFrameworkStatusLogger;

public class PeriodicFrameworkStatusLoggerTest {

	private PeriodicFrameworkStatusLogger statusLogger;
	private BundleContext bundleContext;

	@Before
	public void setUp() throws Exception {
		statusLogger = new PeriodicFrameworkStatusLogger(0);
		bundleContext = Mockito.mock(BundleContext.class);
		Bundle bundle = Mockito.mock(Bundle.class);
		Mockito.when(bundleContext.getBundles()).thenReturn(new Bundle[]{bundle});
		ServiceReference<?> sr = Mockito.mock(ServiceReference.class);
		Mockito.when(bundleContext.getAllServiceReferences(null, null)).thenReturn(new ServiceReference[]{sr});
	}
	
	@Test
	public void activaton_runs_without_error() throws Exception {
		statusLogger.activate(bundleContext);
		Thread.sleep(1000);
	}

	@Test
	public void deactivaton_runs_without_error() throws Exception {
		statusLogger.activate(bundleContext);
		statusLogger.deactivate();
	}

}
