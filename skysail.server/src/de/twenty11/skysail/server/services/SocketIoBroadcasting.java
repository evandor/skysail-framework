package de.twenty11.skysail.server.services;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface SocketIoBroadcasting {

    void send(String message);
}
