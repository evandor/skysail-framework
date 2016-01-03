package io.skysail.server.converter.wrapper;

import java.util.*;

import org.apache.shiro.subject.Subject;

public class STUserWrapper {

    private Subject subject;
    private String peerName;

    public STUserWrapper(Subject subject, String peerName) {
        this.subject = subject;
        this.peerName = peerName;
    }

    public Object getPrincipal() {
        return subject.getPrincipal();
    }

    public Object getUsername() {
        return subject.getPrincipals().asList().get(1);
    }

    public boolean isDeveloper() {
        return subject.hasRole("developer");
    }

    public boolean isAdmin() {
        return subject.hasRole("admin");
    }

    public boolean isDemoUser() {
        return ((String)getUsername()).equals("demo");
    }

    public String getBackend() {
        if (peerName == null || peerName.trim().length() == 0) {
            return "";
        }
        return "["+peerName+"] ";
    }

    public List<String> getPeers() {
       // if (peersProvider == null) {
            return Collections.emptyList();
       // }
    }
}
