package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRevision;

public class Capability implements Comparable<Capability> {

    private Map<String, Object> attributes;
    private Map<String, String> directives;
    private String namespace;
    // @Transient
    private BundleRevision revision;

    // private Reference bundleReference;

    public Capability() {
        // Default constructor, needed for // TODO
    }

    public Capability(BundleCapability cap, Bundle bundle) {
        attributes = cap.getAttributes();
        directives = cap.getDirectives();
        namespace = cap.getNamespace();
        revision = cap.getRevision();
        // this.bundleReference = new Reference(null);// new BundleDescriptor(bundle));
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Map<String, String> getDirectives() {
        return directives;
    }

    public String getNamespace() {
        return namespace;
    }

    // @JsonIgnore
    public BundleRevision getRevision() {
        return revision;
    }

    // public String getBundle() {
    // return bundleReference.toHtmlLink();
    // }

    @Override
    public int compareTo(Capability other) {
        return 0;// serviceId.compareTo(other.getServiceId());
    }

}
