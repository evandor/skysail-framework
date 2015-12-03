package io.skysail.server.utils;

import org.osgi.framework.Bundle;

public interface BundleResourceReader {

    String readResource(Bundle bundle, String path);

}
