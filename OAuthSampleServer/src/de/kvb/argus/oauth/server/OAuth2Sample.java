package de.kvb.argus.oauth.server;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.oauth.internal.*;
import org.restlet.ext.oauth.internal.Client.ClientType;
import org.restlet.ext.oauth.internal.memory.*;

public class OAuth2Sample {
    private static SampleUserManager userManager;

    private static ClientManager clientManager;

    private static TokenManager tokenManager;

    protected static SampleUserManager getSampleUserManager() {
        return userManager;
    }

    protected static ClientManager getClientManager() {
        return clientManager;
    }

    protected static TokenManager getTokenManager() {
        return tokenManager;
    }

    public static void main(String[] args) throws Exception {
        userManager = new SampleUserManager();
        userManager.addUser("alice").setPassword("abcdef".toCharArray());
        userManager.addUser("bob").setPassword("123456".toCharArray());

        clientManager = new MemoryClientManager();
        Client client = clientManager.createClient(ClientType.CONFIDENTIAL, null, null);
        System.out.println("SampleClient: client_id=" + client.getClientId() + ", client_secret="
                + String.copyValueOf(client.getClientSecret()));

        tokenManager = new MemoryTokenManager();

        // Setup Restlet
       // Engine.register(false);
        Component component = new Component();
        component.getClients().add(Protocol.HTTP);
        component.getClients().add(Protocol.HTTPS);
        component.getClients().add(Protocol.RIAP);
        component.getClients().add(Protocol.CLAP);
        component.getServers().add(Protocol.HTTP, 8080);

        component.getDefaultHost().attach("/sample", new SampleApplication());
        OAuth2ServerApplication app = new OAuth2ServerApplication();
        component.getDefaultHost().attach("/oauth", app);
        component.getInternalRouter().attach("/oauth", app);

        component.start();
    }
}
