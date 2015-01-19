package de.twenty11.skysail.api.features.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.api.features.Feature;
import de.twenty11.skysail.api.features.FeatureManager;
import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.features.FeatureUser;
import de.twenty11.skysail.api.features.annotations.EnabledByDefault;
import de.twenty11.skysail.api.features.repository.StateRepository;

@Slf4j
public class FeatureContext {

    private static FeatureManager manager;

    /**
     * Returns the {@link FeatureManager} for the current application (context
     * class loader). The method uses the {@link FeatureManagerProvider} SPI to
     * find the correct {@link FeatureManager} instance. It will throw a runtime
     * exception if no {@link FeatureManager} can be found.
     *
     * @return The {@link FeatureManager} for the application, never
     *         <code>null</code>
     */
    public static synchronized FeatureManager getFeatureManager() {
        if (manager == null) {
            manager = new FeatureManager() {

                @Override
                public List<StateRepository> getStateRepositories() {
                    return null;
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
                    try {
                        Field field = feature.getClass().getDeclaredField(feature.name());
                        Annotation[] annotations = field.getDeclaredAnnotations();
                        String enabledByDefault = EnabledByDefault.class.getName();
                        return Arrays.stream(annotations).filter(a -> {
                            return a.annotationType().getName().equals(enabledByDefault);
                        }).findFirst().isPresent();
                    } catch (NoSuchFieldException | SecurityException e) {
                        log.error(e.getMessage(), e);
                        return false;
                    }
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
