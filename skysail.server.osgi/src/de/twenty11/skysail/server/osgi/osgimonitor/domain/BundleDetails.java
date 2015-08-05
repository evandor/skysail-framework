package de.twenty11.skysail.server.osgi.osgimonitor.domain;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class BundleDetails extends BundleDescriptor {

    private List<ServiceReferenceDetails> registeredServices = new ArrayList<ServiceReferenceDetails>();

    private List<ServiceReferenceDetails> servicesInUse = new ArrayList<ServiceReferenceDetails>();

    // public BundleDetails() {
    // // needed for javax.persistence
    // super();
    // }

    public BundleDetails(Bundle bundle) {
        super(bundle);
        getRegisteredServicesFromBundle(bundle);
        getUsedServicesFromBundle(bundle);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(symbolicName).append(", ");
        return sb.toString();
    }

    public void setRegisteredServices(List<ServiceReferenceDetails> list) {
        this.registeredServices = list;
    }

    public List<ServiceReferenceDetails> getRegisteredServices() {
        return registeredServices;
    }

    public void setServicesInUse(List<ServiceReferenceDetails> list) {
        this.servicesInUse = list;
    }

    public List<ServiceReferenceDetails> getServicesInUse() {
        return servicesInUse;
    }

    // @JsonIgnore
    // public NodeProvider asNode() {
    // return new NodeProvider() {
    //
    // @Override
    // public String getType() {
    // return BundleDetails.this.getClass().getCanonicalName();
    // }
    //
    // @Override
    // public Map<String, String> getNodeProperties() {
    // return Collections.emptyMap();
    // }
    //
    // @Override
    // public String getNodeLabel() {
    // return symbolicName + "(" + version.toString() + ")";
    // }
    //
    // @Override
    // public String getNodeId() {
    // return Long.toString(getBundleId());
    // }
    //
    // @Override
    // public List<EdgeProvider> getEdges() {
    // List<EdgeProvider> result = new ArrayList<EdgeProvider>();
    // for (final ServiceReferenceDetails srd : BundleDetails.this.registeredServices) {
    // if (srd.getUsingBundles().size() == 0) {
    // continue;
    // }
    // for (final BundleDetails usingBundle : srd.getUsingBundles()) {
    // EdgeProvider edgeProvider = new EdgeProvider() {
    //
    // @Override
    // public int getWeight() {
    // return 1;
    // }
    //
    // @Override
    // public String getTarget() {
    // return Long.toString(usingBundle.getBundleId());
    // }
    //
    // @Override
    // public String getSource() {
    // return BundleDetails.this.asNode().getNodeId();
    // }
    //
    // @Override
    // public Map<String, String> getEdgeProperties() {
    // return Collections.emptyMap();
    // }
    //
    // @Override
    // public String getEdgeLabel() {
    // return srd.getName();
    // }
    //
    // @Override
    // public String getEdgeId() {
    // return srd.getName();
    // }
    // };
    // result.add(edgeProvider);
    //
    // }
    // }
    // return result;
    // }
    // };
    // }

    private void getRegisteredServicesFromBundle(Bundle bundle) {
        setRegisteredServices(getDetails(bundle.getRegisteredServices()));
    }

    private void getUsedServicesFromBundle(Bundle bundle) {
        setServicesInUse(getDetails(bundle.getServicesInUse()));
    }

    private List<ServiceReferenceDetails> getDetails(ServiceReference[] serviceReferences) {
        List<ServiceReferenceDetails> serviceDetails = new ArrayList<ServiceReferenceDetails>();
        if (serviceReferences == null) {
            return serviceDetails;
        }
        for (ServiceReference sr : serviceReferences) {
            serviceDetails.add(new ServiceReferenceDetails(sr));
        }
        return serviceDetails;
    }

}
