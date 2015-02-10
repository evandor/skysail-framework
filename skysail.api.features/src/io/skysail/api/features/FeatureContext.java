package io.skysail.api.features;

import io.skysail.api.features.annotations.EnabledByDefault;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureContext {

    private static FeatureManager manager;

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
