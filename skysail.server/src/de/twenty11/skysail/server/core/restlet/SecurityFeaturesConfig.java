//package de.twenty11.skysail.server.core.restlet;
//
//import io.skysail.api.features.FeatureStateRepository;
//import io.skysail.api.features.FeaturesConfig;
//import aQute.bnd.annotation.component.Reference;
//
//public class SecurityFeaturesConfig implements FeaturesConfig {
//
//    private FeatureStateRepository stateRepository;
//
//    @Override
//    public FeatureStateRepository getStateRepository() {
//        return stateRepository;
//    }
//
//    @Reference(dynamic = true, multiple = false, optional = true, target = "(name=SecurityFeatures)")
//    public void setStateRepository(FeatureStateRepository stateRepository) {
//        this.stateRepository = stateRepository;
//    }
//
//    public void unsetStateRepository(FeatureStateRepository stateRepository) {
//        this.stateRepository = null;
//    }
//
//}
