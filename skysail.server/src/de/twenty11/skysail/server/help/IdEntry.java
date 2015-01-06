package de.twenty11.skysail.server.help;

public class IdEntry extends HelpEntry {

    private String cssClass;
    private String id;
    private String text;

    public IdEntry(String id, String text, String cssClass) {
        this.id = id;
        this.text = text;
        this.cssClass = cssClass;
    }

}
