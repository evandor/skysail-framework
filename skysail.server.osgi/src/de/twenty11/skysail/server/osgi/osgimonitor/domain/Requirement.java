package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.Map;

import org.osgi.framework.wiring.BundleRequirement;

public class Requirement {

    private Map<String, Object> attributes;

    public Requirement(BundleRequirement req) {
        attributes = req.getAttributes();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
