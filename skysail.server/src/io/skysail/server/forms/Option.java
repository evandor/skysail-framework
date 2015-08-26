package io.skysail.server.forms;

import java.util.Map.Entry;

import lombok.*;

@Getter
public class Option {

    private String key;
    private String value;
    @Setter
    private boolean selected;

    public Option(Entry<String, String> entry, String currentValue) {
        this.key = entry.getKey();
        this.value = entry.getValue();
        this.selected = key != null ? key.equals(currentValue) : currentValue == null;
    }

}
