package io.skysail.api.peers;

import java.util.List;

public interface PeersProvider {

    List<Peer> getPeers(String principal);

    String getPath(String identifier);

}
