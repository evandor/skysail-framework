package de.twenty11.skysail.api.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Config {

    @JsonIgnore
    private Config parent;

    private String value;
    private String key;
    private List<Config> children = new ArrayList<Config>();

    public Config() {
        parent = null;
    }

    public Config(Config parentConfig, String key, String value) {
        this.parent = parentConfig;
        this.key = key;
        this.value = value;
        if (parentConfig != null) {
            parentConfig.addChild(this);
        }
    }

    public Config(Config parentConfig) {
        this(parentConfig, null, null);
    }

    private void addChild(Config config) {
        this.children.add(config);
    }

    public List<Config> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Config getParent() {
        return parent;
    }

    public String getString(String identifier) {
    	if (identifier.equals(getKey())) {
    		return getValue();
    	}
        for (Config subconf : getChildren()) {
            if (subconf.getKey().equals(identifier)) {
                return subconf.getValue();
            }
        }
        return null; // NOSONAR
    }

    public List<Config> get(String identifier) {
        for (Config subconf : getChildren()) {
            if (subconf.getKey().equals(identifier)) {
                return subconf.getChildren();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int indention) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indention; i++) {
            sb.append(" ");
        }
        sb.append(key);
        if (value != null) {
            sb.append(" -> ").append(value);
        }
        sb.append(",");
        if (!children.isEmpty()) {
            sb.append(" #").append(children.size()).append(" children:\n");
        } else {
            sb.append("\n");
        }
        for (Config childConfig : children) {

            sb.append(childConfig.toString(indention + 2));
        }
        return sb.toString();
    }
}
