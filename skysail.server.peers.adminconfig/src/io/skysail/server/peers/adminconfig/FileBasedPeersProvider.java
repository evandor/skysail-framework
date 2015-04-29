package io.skysail.server.peers.adminconfig;

import io.skysail.api.peers.PeersProvider;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
@Slf4j
public class FileBasedPeersProvider implements PeersProvider {

    private volatile Map<String, String> config;

    @Activate
    public void activate(Map<String, String> config) {
        this.config = config;
    }

    @Deactivate
    public void deactivate() {
        this.config = null;
    }
}
