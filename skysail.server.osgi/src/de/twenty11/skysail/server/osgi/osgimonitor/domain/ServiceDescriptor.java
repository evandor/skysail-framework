package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.restlet.data.Reference;

public class ServiceDescriptor implements Comparable<ServiceDescriptor> {

    private static final String SERVICE_ID_IDENTIFIER = "service.id";

    private Long serviceId = -1L;
    private Map<String, Object> properties = new HashMap<String, Object>();
    // private de.twenty11.skysail.common.navigation.Reference providingBundle;

    @Transient
    private Map<String, String> links;

    @Transient
    private List<BundleDescriptor> usingBundles = new ArrayList<BundleDescriptor>();

    @Transient
    private Reference reference;

    /**
     * Default constructor, needed for // TODO
     */
    public ServiceDescriptor() {
    }

    public ServiceDescriptor(ServiceReference sr, Reference reference) {
        // providingBundle = new de.twenty11.skysail.common.navigation.Reference(null);// new
        // BundleDescriptor(sr.getBundle()));
        this.reference = reference;
        handleUsingBundles(sr, reference);
        handlePropertyKeys(sr);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public int compareTo(ServiceDescriptor other) {
        return serviceId.compareTo(other.getServiceId());
    }

    public Long getServiceId() {
        return serviceId;
    }

    // public String getProvidingBundle() {
    // return providingBundle.toHtmlLink();
    // }

    @Override
    public String toString() {
        return serviceId + " [" + ((Object[]) properties.get("objectClass"))[0] + "]";
    }

    private void handleUsingBundles(ServiceReference sr, Reference reference) {
        if (sr.getUsingBundles() != null) {
            for (Bundle usingBundle : sr.getUsingBundles()) {
                usingBundles.add(null);// new BundleDescriptor(usingBundle));
            }
        }
    }

    private void handlePropertyKeys(ServiceReference sr) {
        String[] propertyKeys = sr.getPropertyKeys();
        for (String key : propertyKeys) {
            if (SERVICE_ID_IDENTIFIER.equals(key)) {
                serviceId = (Long) sr.getProperty(SERVICE_ID_IDENTIFIER);
            } else {
                properties.put(key, sr.getProperty(key));
            }
        }
    }

}
