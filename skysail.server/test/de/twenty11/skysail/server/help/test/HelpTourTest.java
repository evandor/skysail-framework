package de.twenty11.skysail.server.help.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.help.HelpEntry;
import de.twenty11.skysail.server.help.HelpTour;

public class HelpTourTest {

    private HelpTour helpTour;

    @Before
    public void setUp() throws Exception {
        helpTour = new HelpTour("title");
    }

    @Test
    public void renders_empty_tour() throws Exception {
        helpTour.addEntry().withId("id").andText("text").andCssClass("css");
        String asJoyRide = helpTour.asJoyRide();
        assertThat(asJoyRide, containsString("<ol id=\"title\">"));
        assertThat(asJoyRide, containsString("data-id=\"id\""));
        assertThat(asJoyRide, not(containsString("data-class")));
    }

    @Test
    public void renders_tour_with_text() throws Exception {
        HelpEntry entry = helpTour.addEntry().withClass("cls").andText("text").andCssClass("css");
        entry.addText("entryText");
        String asJoyRide = helpTour.asJoyRide();
        assertThat(asJoyRide, containsString("data-class=\"cls\""));
        assertThat(asJoyRide, containsString("entryText"));
        assertThat(asJoyRide, not(containsString("data-id")));
    }

}
