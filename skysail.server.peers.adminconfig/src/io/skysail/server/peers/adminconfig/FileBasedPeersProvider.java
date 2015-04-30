package io.skysail.server.peers.adminconfig;

import io.skysail.api.peers.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class FileBasedPeersProvider implements PeersProvider {

    private volatile Map<String, String> config;
    private volatile List<Peer> peers = new ArrayList<>();

    @Activate
    public void activate(Map<String, String> config) {
        this.config = config;
        String installations = config.get("installations");
        if (installations == null || installations.length() == 0) {
            log.warn("no installations are defined");
            return;
        }
        createPeersFromConfig(installations, config);
    }

    @Deactivate
    public void deactivate() {
        this.config = null;
    }

    @Override
    public List<Peer> getPeers(String username) {
        return peers.stream().filter(p -> {
            return p.users().contains(username);
        }).collect(Collectors.toList());
    }

    private void createPeersFromConfig(String installations, Map<String, String> conf) {
        Arrays.stream(installations.split(",")).map(u -> {
            return u.trim();
        }).forEach(installation -> {
            String name = config.get(installation + ".name");
            String protocol = config.get(installation + ".protocol");
            String host = config.get(installation + ".host");
            List<String> users = Arrays.stream(config.get(installation + ".users").split(",")).map(u -> {
                return u.trim();
            }).collect(Collectors.toList());
            peers.add(new Peer().name(name).protocol(protocol).host(host).users(users));
        });
    }

    @Override
    public String getPath(String identifier) {
        return peers.stream().filter(p -> {
            return p.name().equals(identifier);
        }).map(p -> {
            return p.protocol() + "://" + p.host();
        }).findFirst().orElseThrow(IllegalStateException::new);
    }
}
