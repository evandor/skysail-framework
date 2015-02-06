package de.twenty11.skysail.server.ext.converter.st.helper;

public final class Breadcrumb {

    private final String href;
    private final String cssClass;
    private final String value;

    public Breadcrumb(String href, String cssClass, String value) {
        this.href = href;
        this.cssClass = cssClass;
        this.value = value;
    }

    public String getHref() {
        return href;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(value);
        sb.append(" (").append(href).append(")");
        return sb.toString();
    }
}
