package io.skysail.server.app.plugins.resources;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.osgi.framework.Bundle;

@Getter
@NoArgsConstructor
public class Resource {

    private enum InstalledBundleMatch {
        NONE, NAME, VERSION
    }

    private String symbolicName;
    private String version;
    private Long size;
    private InstalledBundleMatch installedBundleMatch = InstalledBundleMatch.NONE;
    private String id;
    private String uri;

    public Resource(org.apache.felix.bundlerepository.Resource r, List<Bundle> installedBundles) {
        symbolicName = r.getSymbolicName();
        version = r.getVersion().toString();
        size = r.getSize();
        id = symbolicName + ";" + version;

        uri = r.getURI();
        if (installedBundles.stream().filter(installedBundle -> {
            return installedBundle.getSymbolicName().equals(symbolicName);
        }).findAny().isPresent()) {
            installedBundleMatch = InstalledBundleMatch.NAME;
        }
        if (installedBundles
                .stream()
                .filter(installedBundle -> {
                    return installedBundle.getSymbolicName().equals(symbolicName)
                            && installedBundle.getVersion().toString().equals(version);
                }).findAny().isPresent()) {
            installedBundleMatch = InstalledBundleMatch.VERSION;
        }

    }

}
