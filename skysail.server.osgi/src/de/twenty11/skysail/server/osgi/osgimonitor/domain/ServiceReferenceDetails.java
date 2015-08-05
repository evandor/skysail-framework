package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.ServiceReference;


public class ServiceReferenceDetails implements Serializable {

    private static final long serialVersionUID = -3727775505031420491L;
    private Map<String, String> properties = new HashMap<String, String>();
    private String name;
    private List<BundleDetails> usingBundles;
    private long bundleId;

    public ServiceReferenceDetails() {
    }

    public ServiceReferenceDetails(ServiceReference sr) {
        // this.usingBundles = Arrays.asList(sr.getUsingBundles());
        this.bundleId = sr.getBundle().getBundleId();
        this.name = sr.toString();
        String[] propertyKeys = sr.getPropertyKeys();
        for (String key : propertyKeys) {
            properties.put(key, sr.getProperty(key).toString());
        }
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setName(String string) {
        this.name = string;
    }

    public String getName() {
        return name;
    }

    public void setUsingBundles(List<BundleDetails> details) {
        this.usingBundles = details;
    }

    public List<BundleDetails> getUsingBundles() {
        return usingBundles;
    }

    public void setBundleId(long bundleId) {
        this.bundleId = bundleId;
    }

    public long getBundleId() {
        return bundleId;
    }

}
