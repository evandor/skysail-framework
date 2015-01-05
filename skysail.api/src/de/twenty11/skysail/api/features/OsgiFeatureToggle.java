package de.twenty11.skysail.api.features;


import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * An OsgiFeatureToggle is a {@link FeatureToggle} which uses a {@link FeatureManager} provided from the OSGi registry.
 * 
 * Define it like
 * 
 * <pre>
 * <code>
 * @aQute.bnd.annotation.component.Component
 * public class MyFeature extends OsgiFeatureToggle {
 *
 *   public MyFeature() {
 *       setStateRepository("FileBasedStateRepository");
 *   }
 *
 *   public static boolean myIsActive() {
 *      return featureManager != null ? featureManager
 *          .isActive(new MyFeature()) : false;
 *   }
 * }
 * </code>
 * </pre>
 *
 * and call it like
 * 
 * <pre><code>if (MyFeature.myIsActive()) {...}</code></pre>.
 */
@Component(immediate = true)
public class OsgiFeatureToggle implements FeatureToggle {

	protected static FeatureManager featureManager;
	private String repositoryName;

	public boolean isActive() {
		if (featureManager == null) {
			return false;
		}
		return featureManager.isActive(this);
	}

	public StateRepository getStateRepository()  {
		for (StateRepository repo : featureManager.getStateRepositories()) {
            if (repo.getClass().getSimpleName().equals(repositoryName)) {
                return repo;
            }
        }
        return null;
	}

	protected void setStateRepository(String repo) {
		this.repositoryName = repo;
    }

	@Override
	public String name() {
		return this.getClass().getName();
	}

	@Reference(multiple = true, dynamic = true, optional = true)
	public void setFeatureManager(FeatureManager featureManager) {
		OsgiFeatureToggle.featureManager = featureManager;
	}

	public void unsetFeatureManager(@SuppressWarnings("unused") FeatureManager featureManager) {
		OsgiFeatureToggle.featureManager = null;
	}

}
