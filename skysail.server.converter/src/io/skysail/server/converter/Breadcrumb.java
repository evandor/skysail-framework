package io.skysail.server.converter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Breadcrumb {

    private final String href;
    private final String cssClass;
    private final String value;
    private final Boolean favorite;

    public String getHref() {
        return href;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getValue() {
        return value;
    }

    public boolean isFavoriteEmpty() {
        return favorite != null && !favorite;
    }

    public boolean isFavoriteFull() {
        return favorite != null && favorite;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(value);
        sb.append(" (").append(href).append(")");
        return sb.toString();
    }
}
