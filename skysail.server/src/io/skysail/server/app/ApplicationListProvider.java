package io.skysail.server.app;

import java.util.List;

import io.skysail.server.SkysailComponent;

@org.osgi.annotation.versioning.ProviderType
public interface ApplicationListProvider {

    List<SkysailApplication> getApplications();

    void attach(SkysailComponent skysailComponent);

    void detach(SkysailComponent skysailComponent);
}
