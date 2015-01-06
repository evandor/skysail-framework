package de.twenty11.skysail.server.help;

public class HelpEntry {

    StringBuilder helpText = new StringBuilder();

    private String id;
    private String text;
    private String cssClass;
    private String button;
    private String options;
    private String cls;

    public void addText(String text) {
        helpText.append("      ").append(text).append("\n");
    }

    public HelpEntry withId(String id) {
        this.id = id;
        return this;
    }

    public HelpEntry withClass(String cls) {
        this.cls = cls;
        return this;
    }

    public HelpEntry andText(String text) {
        this.text = text;
        return this;
    }

    public HelpEntry andCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public HelpEntry andButton(String button) {
        this.button = button;
        return this;
    }

    public HelpEntry andOptions(String options) {
        this.options = options;
        return this;
    }

    public String asJoyRide() {
        StringBuilder sb = new StringBuilder();
        sb.append("    <li ");
        sb.append(setIfNotNull(id, "data-id"));
        sb.append(setIfNotNull(cls, "data-class"));
        sb.append(setIfNotNull(text, "data-text"));
        sb.append(setIfNotNull(cssClass, "class"));
        sb.append(setIfNotNull(button, "data-button"));
        sb.append(setIfNotNull(options, "data-options"));
        sb.append(">\n");
        sb.append(helpText);
        sb.append("\n    </li>\n");
        return sb.toString();
    }

    private String setIfNotNull(String ident, String html) {
        return ident != null ? html + "=\"" + ident + "\" " : "";
    }

}
