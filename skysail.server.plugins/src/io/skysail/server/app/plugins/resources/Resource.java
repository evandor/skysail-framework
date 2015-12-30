package io.skysail.server.app.plugins.resources;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;

import java.util.List;

import lombok.*;

import org.osgi.framework.Bundle;

@Getter
@NoArgsConstructor
public class Resource implements Identifiable {

    private enum InstalledBundleMatch {
        NONE, NAME, VERSION
    }

    @Field
    private String symbolicName;

    @Field
    private String version;

    private Long size;
    private InstalledBundleMatch installedBundleMatch = InstalledBundleMatch.NONE;

    @Field
    private String bundleId;

    @Field
    private String id;

    private String uri;

    public Resource(org.apache.felix.bundlerepository.Resource r, List<Bundle> installedBundles) {
        symbolicName = r.getSymbolicName();
        version = r.getVersion().toString();
        size = r.getSize();
        id = symbolicName + ";" + version;
        bundleId = r.getId();
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

    @Override
    public void setId(String id) {
    }

}
