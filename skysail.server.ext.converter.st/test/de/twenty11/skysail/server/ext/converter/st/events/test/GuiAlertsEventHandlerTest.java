//package de.twenty11.skysail.server.ext.converter.st.events.test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.osgi.service.event.Event;
//
//import de.twenty11.skysail.server.ext.converter.st.events.GuiAlertsEventHandler;
//
//public class GuiAlertsEventHandlerTest {
//
//	private GuiAlertsEventHandler eventHandler;
//
//	@Before
//	public void setUp() throws Exception {
//		eventHandler = new GuiAlertsEventHandler();
//	}
//	
//	@Test
//    public void testName() throws Exception {
//		Map<String, String> map = new HashMap<>();
//		map.put("msg", "msg");
//		Event event = new Event("GUI/alerts/warning", map);
//	    eventHandler.handleEvent(event);
//    }
//
//}
