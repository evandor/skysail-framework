package de.twenty11.skysail.server.app;

import java.util.List;

import de.twenty11.skysail.server.SkysailComponent;

public interface ApplicationListProvider {

    List<SkysailApplication> getApplications();

    void attach(SkysailComponent skysailComponent);

    void detach(SkysailComponent skysailComponent);
}
