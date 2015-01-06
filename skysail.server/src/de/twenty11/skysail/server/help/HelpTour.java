package de.twenty11.skysail.server.help;

import java.util.ArrayList;
import java.util.List;

public class HelpTour {

    private String title;
    private List<HelpEntry> tour = new ArrayList<HelpEntry>();

    public HelpTour(String title) {
        this.title = title;
    }

    public HelpEntry addEntry() {
        HelpEntry entry = new HelpEntry();
        this.tour.add(entry);
        return entry;
    }

    public String asJoyRide() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  <ol id=\"").append(title).append("\">\n");
        for (HelpEntry helpEntry : tour) {
            sb.append(helpEntry.asJoyRide());
        }
        sb.append("\n  </ol>\n");

        sb.append(javascriptCode());

        return sb.toString();
    }

    private String javascriptCode() {
        StringBuilder sb = new StringBuilder();

        sb.append("  <script type=\"text/javascript\" src=\"/../static/js/joyride/modernizr.mq.js\"></script>\n");
        sb.append("  <script type=\"text/javascript\" src=\"/../static/js/joyride/jquery.joyride-2.1.js\"></script>\n");
        sb.append("  <script>\n");
        sb.append("    $(\"#startTour\").click(function() {\n");
        sb.append("      $('#joyRideTipContent').joyride({\n");
        sb.append("        autoStart : true,\n");
        // postStepCallback : function (index, tip) {
        // if (index == 2) {
        // $(this).joyride('set_li', false, 1);
        // }
        // },
        sb.append("        modal:true,\n");
        sb.append("        expose: true\n");
        sb.append("      })\n");
        sb.append("    })\n");
        sb.append("  </script>\n");
        return sb.toString();
    }
}
