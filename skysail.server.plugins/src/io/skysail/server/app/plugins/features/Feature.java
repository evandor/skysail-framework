package io.skysail.server.app.plugins.features;

import java.util.ArrayList;
import java.util.List;

public class Feature {

    private String id;
    
	private String name;
	
	private String version;
	
	private FeatureStatus status;

    private List<String> locations = new ArrayList<>();

    private String landingPage;

    private boolean openInNewWindow;
    
	public Feature(String location) {
		this.locations.add(location);
    }

	public Feature() {
    }

	public String getName() {
		return name;
	}
	
	public void setId(String id) {
        this.id = id;
    }
	
	public String getId() {
        return id;
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public FeatureStatus getStatus() {
		return status;
	}

	public void setStatus(FeatureStatus status) {
		this.status = status;
	}

    public void addBundleLocation(String location) {
        locations .add(location);
    }
	
	public List<String> getLocations() {
        return locations;
    }
	
	public String getLandingPage() {
        return landingPage;
    }
	
	public void setLandingPage(String landingPage) {
        this.landingPage = landingPage;
    }

    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }
    
    public void setOpenInNewWindow(boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }
}
