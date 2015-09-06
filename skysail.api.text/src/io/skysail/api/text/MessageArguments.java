package io.skysail.api.text;

import java.util.*;

import lombok.Getter;

import org.restlet.resource.Resource;

@Getter
public class MessageArguments {

    public class ArgumentDesc {

        Map<String, Object> map = new HashMap<>();

        public void add(String description, Object value) {
            map.put(description, value);
        }

        public Collection<Object> getArguments() {
            return map.values();
        }
    }

    private Map<String, ArgumentDesc> arguments = new HashMap<>();
    private ArgumentDesc argument;

    public MessageArguments(Class<? extends Resource> cls) {
        argument = new ArgumentDesc();
        arguments.put(cls.getName() + ".message", argument);
    }

    public MessageArguments add(String description, Object value) {
        argument.add(description, value);
        return this;
    }

    public MessageArguments setNewIdentifier(String identifier) {
        argument = new ArgumentDesc();
        arguments.put(identifier, argument);
        return this;
    }

    public Set<String> getIdentifier() {
        return Collections.unmodifiableSet(arguments.keySet());
    }

    public Collection<Object> get(String resourceMessageIdentifier) {
        ArgumentDesc argumentDesc = arguments.get(resourceMessageIdentifier);
        if (argumentDesc != null) {
            return argumentDesc.getArguments();
        }
        return Collections.emptySet();
    }

}
