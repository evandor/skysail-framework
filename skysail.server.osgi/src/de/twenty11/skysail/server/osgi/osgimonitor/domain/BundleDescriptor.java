package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;

import java.io.Serializable;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

import org.osgi.framework.*;

/**
 * The meta-information about a {@link Bundle}.
 */
public class BundleDescriptor implements Serializable, Identifiable {

    private static final long serialVersionUID = -3490514848138717614L;

    private static TimeZone tz = TimeZone.getTimeZone("UTC");

    @Field
    private long bundleId;

    @Field
    protected String symbolicName;

    @Field
    protected Version version;

    @Field
    protected long lastModified;

    @Field
    protected int state;

    private List<BundleEvent> events = new ArrayList<BundleEvent>();

    private List<Long> usingBundles;

    /**
     * Initialization with concrete bundle.
     *
     * @param bundle
     *            to describe
     */
    public BundleDescriptor(Bundle bundle) {
        this.bundleId = bundle.getBundleId();
        this.symbolicName = bundle.getSymbolicName();
        this.version = bundle.getVersion();
        this.lastModified = bundle.getLastModified();
        this.state = bundle.getState();
        this.usingBundles = new ArrayList<>();
        if (bundle.getRegisteredServices() != null) {
            this.usingBundles = Arrays.stream(bundle.getRegisteredServices()).map(service -> {
                return service.getUsingBundles();
            }).filter(bundles -> bundles != null).flatMap(bundles -> Arrays.stream(bundles)).map(b -> b.getBundleId())
                    .collect(Collectors.toList());
        }
    }

    private String mapState(int state) {
        switch (state) {
        case 1:
            return "Uninstalled";
        case 2:
            return "Installed";
        case 4:
            return "Resolved";
        case 8:
            return "Starting";
        case 16:
            return "Stopping";
        case 32:
            return "Active";
        default:
            return "unknown state";
        }
    }

    private String handleVersion(Version version) {
        StringBuffer sb = new StringBuffer();
        sb.append(version.getMajor()).append(".");
        sb.append(version.getMinor()).append(".");
        sb.append(version.getMicro());
        if (version.getQualifier() != null && version.getQualifier().trim() != "") {
            sb.append(".");
            sb.append(version.getQualifier());
        }
        return sb.toString();
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastModified() {
        return lastModified;
    }

    // @JsonSetter
    // public void setState(String state) {
    // this.state = state;
    // }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getState() {
        return mapState(state);
    }

    public void setVersion(Version version) {
        this.version = version;// handleVersion(version);
    }

    public String getVersion() {
        return handleVersion(version);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return "[" + sdf.format(new Date(getLastModified())) + "] " + getSymbolicName() + " (" + getVersion() + ")";
        // "bundles/details/"
        // + this.bundleId;
    }

    public void addEvent(BundleEvent event) {
        events.add(event);
    }

    public String getLastModifiedAsIso8601() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date(getLastModified()));
    }

    public List<BundleEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public List<Long> getUsingBundles() {
        return usingBundles;
    }

    public String getVisEdgesForUsingBundles() {
        if (usingBundles.size() == 0) {
            return null;
        }
        return usingBundles.stream().map(bundle -> {
            return "{from: " + this.bundleId + ", to: "+bundle+"},";
        }).collect(Collectors.joining());
    }

    public long getBundleId() {
        return bundleId;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
