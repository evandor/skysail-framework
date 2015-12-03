package io.skysail.server.utils;

import org.osgi.framework.Bundle;

public class DefaultBundleResourceReader implements BundleResourceReader {

    @Override
    public String readResource(Bundle bundle, String path) {
        return BundleUtils.readResource(bundle, path);
    }

}
