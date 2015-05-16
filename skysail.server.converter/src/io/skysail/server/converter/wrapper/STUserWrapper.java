package io.skysail.server.converter.wrapper;

import io.skysail.api.peers.*;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.shiro.subject.Subject;

public class STUserWrapper {

    private Subject subject;
    private PeersProvider peersProvider;
    private String peerName;

    public STUserWrapper(Subject subject, PeersProvider peersProvider, String peerName) {
        this.subject = subject;
        this.peersProvider = peersProvider;
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
    
    public String getBackend() {
        if (peerName == null || peerName.trim().length() == 0) {
            return "";
        }
        return "["+peerName+"] ";
    }

    public List<String> getPeers() {
        if (peersProvider == null) {
            return Collections.emptyList();
        }
        Optional<String> username = subject.getPrincipals().asList().stream().filter(p -> {
            System.out.println(p.toString());
            return (!(p.toString().startsWith(("#"))));
        }).findFirst();
        if(username.isPresent()) {
            List<Peer> peers = peersProvider.getPeers(username.get());
            return peers.stream().map(p -> {
                return p.name();
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
            
    }
}
