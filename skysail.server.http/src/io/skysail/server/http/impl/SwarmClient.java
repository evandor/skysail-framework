package io.skysail.server.http.impl;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.*;
import org.osgi.service.component.annotations.*;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import lombok.extern.slf4j.Slf4j;

@org.osgi.service.component.annotations.Component(immediate = true, property = { "service.pid=peers" })
@Slf4j
public class SwarmClient implements ManagedService {

    ThreadManager tm = new ThreadManager();
    private Dictionary<String, ?> properties;
    boolean started = false;

    private static class Heartbeat extends ThreadManager.Interruptible {

        private volatile boolean active = true;
        private String host;
        private String port;
        private String id;

        public Heartbeat(String host, String port, String id) {
            this.host = host;
            this.port = port;
            this.id = id;
        }

        @Override
        public void run() {
            while (active) {
                ping(host, port, id);
                try {
                    Thread.sleep(60 * 10000);
                } catch (Exception e) {
                }
            }
        }

        private void ping(String host, String port, String id) {
            String url = host;
            if (port != null && port.trim().length() > 0) {
                url += ":" + port;
            }
            url += "/LookupServerApp/installations/" + id + "/ip/";
            log.info("heartbeat request to '{}'", url);
            try {
                ClientResource cr = new ClientResource(url);
                Representation representation = cr.post("");
            } catch (Exception e) {
                log.warn("heartbeat problem: {}", e.getMessage());
            }
        }

        @Override
        public void interrupt() {
            
        }
    }

    @Activate
    public void activate() {
        if (properties != null && !started) {
            startHeartbeats();
        }
    }

    @Deactivate
    public void deactivate() {
        stop();
        started = false;
    }

    private synchronized void startHeartbeats() {
        log.info("checking 'peers' configuration to connect to swarm servers...");

        Set<String> peers = new HashSet<>();
        Collections.list(properties.keys()).stream().filter(key -> {
            return key.startsWith("peers.");
        }).forEach(key -> {
            peers.add(key.substring(0, StringUtils.ordinalIndexOf(key, ".", 2)));
        });

        peers.forEach(peer -> {
            String host = (String) properties.get(peer + ".host");
            String port = (String) properties.get(peer + ".port");
            String id = (String) properties.get(peer + ".id");
            if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port) || StringUtils.isEmpty(id)) {
                log.warn("not able to start heartbeat thread for swarm server #{} on '{}:{}'", new Object[] { id, host,
                        port });
                return;
            }
            log.info("creating new heartbeat thread for swarm server '{}:{}'", host, port);
            tm.run(new Heartbeat(host, port, id), "Heartbeat for '" + host + ":" + port + "', id " + id);
        });
        started = true;
    }

    public void stop() {
        try {
            tm.stopAll();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        this.properties = properties;
        if (properties != null) {
            startHeartbeats();
        }
    }

}
