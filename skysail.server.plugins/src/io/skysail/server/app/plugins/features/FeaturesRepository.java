package io.skysail.server.app.plugins.features;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FeaturesRepository {

    public List<Feature> getFeatures() {
        Feature feature = new Feature();
        feature.setId("123");
        feature.setName("web console");
        feature.setVersion("1.2.3");
        feature.setStatus(FeatureStatus.AVAILABLE);

        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.http.jetty/org.apache.felix.http.jetty-2.3.0.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.http.bundle/org.apache.felix.http.bundle-2.3.0.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.webconsole/org.apache.felix.webconsole-4.2.2.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.webconsole.plugins.memoryusage/org.apache.felix.webconsole.plugins.memoryusage-1.0.4.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.webconsole.plugins.ds/org.apache.felix.webconsole.plugins.ds-1.0.0.jar");
        // feature.addBundleLocation(" https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.servicediagnostics.plugin/org.apache.felix.servicediagnostics.plugin-0.1.3.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.dependencymanager/org.apache.felix.dependencymanager-3.1.0.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.dependencymanager.runtime/org.apache.felix.dependencymanager.runtime-3.1.0.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.webconsole.plugins.obr/org.apache.felix.webconsole.plugins.obr-1.0.0.jar");
        feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.bundlerepository/org.apache.felix.bundlerepository-2.0.2.jar");

        feature.setLandingPage("http://localhost:8080/system/console/");
        feature.setOpenInNewWindow(true);

        Feature feature2 = new Feature();
        feature2.setId("345");
        feature2.setName("notes");
        feature2.setVersion("0.5.6");
        feature2.setStatus(FeatureStatus.AVAILABLE);

        // feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.http.jetty/org.apache.felix.http.jetty-2.3.0.jar");
        // feature.addBundleLocation("https://github.com/evandor/skysail-repository/raw/master/release/org.apache.felix.http.bundle/org.apache.felix.http.bundle-2.3.0.jar");

        // Feature feature3 = new Feature(
        // "https://github.com/evandor/skysail-repository/raw/master/release/skysail.server.ext.dbclient/skysail.server.ext.dbclient-1.0.0.jar");
        // Feature feature3 = new
        // Feature("file:///Users/carsten/git/skysail/skysail.server.ext.dbclient/generated/skysail.server.ext.dbclient.jar");
        Feature feature3 = new Feature(
                "file:/C:/git/skysail/skysail.server.ext.dbclient/generated/skysail.server.ext.dbclient.jar");
        feature3.setName("DbClient");
        feature3.setId("235");
        feature3.setStatus(FeatureStatus.AVAILABLE);

        Feature feature4 = getFeature4();
        return Arrays.asList(feature, feature2, feature3, feature4);
    }

    private Feature getFeature4() {
        Feature feature = new Feature(
                "file:///Users/carsten/git/skysail/skysail.server.app.wiki/generated/skysail.server.app.wiki.jar");
        feature.setName("Wiki");
        feature.setId("2355");
        feature.setStatus(FeatureStatus.AVAILABLE);
        return feature;
    }

    public Optional<Feature> getFeature(String featureId) {
        return getFeatures().stream().filter(f -> f.getId().equals(featureId)).findFirst();
    }

}
