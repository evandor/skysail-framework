package io.skysail.api.peers;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = true)
public class Peer {

    private String name;
    private String protocol = "http";
    private String host;
    private List<String> users;
}
