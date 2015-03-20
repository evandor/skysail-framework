package io.skysail.api.forms.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import io.skysail.api.forms.IgnoreSelectionProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IgnoreSelectionProviderTest {
	
	@Mock
	private Object configuration;

	@InjectMocks
	private IgnoreSelectionProvider ignoreSelectionProvider;

	@Test
	public void testName() throws Exception {
		assertThat(ignoreSelectionProvider.getSelections().size(), is(0));
	}
}
