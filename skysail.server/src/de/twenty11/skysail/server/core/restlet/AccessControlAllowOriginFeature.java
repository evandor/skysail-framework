package de.twenty11.skysail.server.core.restlet;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.api.features.OsgiFeatureToggle;

@Component
public class AccessControlAllowOriginFeature extends OsgiFeatureToggle {

    // private static FeatureManager featureManager;

    public AccessControlAllowOriginFeature() {
        setStateRepository("FileBasedStateRepository");
    }

    public static boolean myIsActive() {
        return featureManager != null ? featureManager.isActive(new AccessControlAllowOriginFeature()) : false;
    }

}