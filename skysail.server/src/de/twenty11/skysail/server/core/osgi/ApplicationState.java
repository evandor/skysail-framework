package de.twenty11.skysail.server.core.osgi;

public enum ApplicationState {
    // @formatter:off
    NEW      ("application has just been added to skysail"), 
    ATTACHED ("application has been attached to restlet component"), 
    DETACHED ("application is detached from restlet component");
    // @formatter:on

    private String desc;

    ApplicationState(String desc) {
        this.desc = desc;
    }
}
