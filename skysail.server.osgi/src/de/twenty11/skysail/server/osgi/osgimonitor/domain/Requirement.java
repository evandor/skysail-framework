package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import io.skysail.api.domain.Identifiable;

import java.util.Map;

import org.osgi.framework.wiring.BundleRequirement;

public class Requirement implements Identifiable {

    private Map<String, Object> attributes;

    public Requirement(BundleRequirement req) {
        attributes = req.getAttributes();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
