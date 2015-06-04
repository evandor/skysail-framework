package de.twenty11.skysail.server.core;

import java.util.Map.Entry;

import lombok.Getter;

@Getter
public class Option {

    private String key;
    private String value;
    private boolean selected;

    public Option(Entry<String, String> entry, String currentValue) {
        this.key = entry.getKey();
        this.value = entry.getValue();
        this.selected = value != null ? value.equals(currentValue) : currentValue == null;
    }

}
