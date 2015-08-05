package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.cm.Configuration;

public class ConfigDescriptor {

    private String bundleLocation;
    private String factoryPid;
    private String pid;
    private Dictionary properties;

    public ConfigDescriptor() {
    }

    public ConfigDescriptor(Configuration conf) {
        bundleLocation = conf.getBundleLocation();
        factoryPid = conf.getFactoryPid();
        pid = conf.getPid();
        properties = conf.getProperties();
    }

    public String getBundleLocation() {
        return bundleLocation;
    }

    public String getFactoryPid() {
        return factoryPid;
    }

    public String getPid() {
        return pid;
    }

    public Map<String, String> getProperties() {
        Map<String, String> result = new HashMap<String, String>();
        Enumeration keys = this.properties.keys();
        while (keys.hasMoreElements()) {
            Object nextElement = keys.nextElement();
            result.put(nextElement.toString(), this.properties.get(nextElement).toString());
        }
        return result;
    }

}
