package io.skysail.api.features;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Slf4j
@Component(immediate = true)
public class FeatureContext {

    private static List<FeatureStateRepository> featureRepositories = new ArrayList<>();

    // for now: the one and only featureManager 
    private static FeatureManager manager;

    @Reference(multiple = true, dynamic = true, optional = true)
    public void addFeaturesStateRepository(FeatureStateRepository repo) {
        featureRepositories.add(repo);
    }

    public void removeFeaturesStateRepository(FeatureStateRepository repo) {
        featureRepositories.remove(repo);
    }

    /**
     * Returns the {@link FeatureManager} for the current application.
     *
     * @return The {@link FeatureManager} for the application, never
     *         <code>null</code>
     */
    public static synchronized FeatureManager getFeatureManager() {
        if (manager == null) {
            manager = new FeatureManager() {

                @Override
                public List<FeatureStateRepository> getStateRepositories() {
                    return featureRepositories;
                }

                @Override
                public String getName() {
                    return null;
                }

                @Override
                public FeatureUser getCurrentFeatureUser() {
                    return null;
                }

                @Override
                public boolean isActive(Feature feature) {
                    // Field field =
                    // feature.getClass().getDeclaredField(feature.name());
                    // Annotation[] annotations =
                    // field.getDeclaredAnnotations();
                    // String enabledByDefault =
                    // EnabledByDefault.class.getName();
                    // Arrays.stream(annotations).filter(a -> {
                    // return
                    // a.annotationType().getName().equals(enabledByDefault);
                    // }).findFirst().isPresent();

                    // FeatureState featureState =
                    // getFeatureManager().getFeatureState(feature);
                    // return featureState.isEnabled();

                    Optional<FeatureState> featureState = featureRepositories.stream().map(repo -> {
                        return repo.getFeatureState(feature);
                    }).filter(state -> {
                        return state != null;
                    }).findFirst();

                    if (!featureState.isPresent()) {
                        return false;// getMetaData(feature).isEnabledByDefault();
                    }
                    FeatureState state = featureState.get();
                    if (state.isEnabled()) {

//                        String strategyId = state.getStrategyId();
//                        if (strategyId == null || strategyId.isEmpty()) {
//                            return true;
//                        }
                        return true;

                        // FeatureUser user =
                        // userProvider.getCurrentUser();

                        // check the selected strategy
                        // for (ActivationStrategy strategy :
                        // strategyProvider.getActivationStrategies()) {
                        // if (strategy.getId().equalsIgnoreCase(strategyId)) {
                        // return strategy.isActive(state, user);
                        // }
                        // }

                    }

                    // state.get

                    return false;

                }

                @Override
                public FeatureState getFeatureState(Feature feature) {
                    return null;
                }

                @Override
                public void setFeatureState(FeatureState state) {

                }

                @Override
                public Set<Feature> getFeatures() {
                    return null;
                }
            };
        }
        return manager;
    }
}
