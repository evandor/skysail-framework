/**
 *  Copyright 2011 Carsten GrGraeff
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.twenty11.skysail.server.core.osgi.internal.listener;

import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.*;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.services.SocketIoBroadcasting;

/**
 * listener for bundle events.
 *
 */
@Component(immediate = true)
@Slf4j
public class SkysailServerBundleListener implements BundleListener {// NO_UCD

    private AtomicReference<SocketIoBroadcasting> socketIoBroadcastingRef = new AtomicReference<>();

    @Activate
    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        componentContext.getBundleContext().addBundleListener(this);
    }

    @Deactivate
    protected synchronized void deactivate(ComponentContext componentContext) {
        if (componentContext != null) {
            componentContext.getBundleContext().removeBundleListener(this);
        }
    }

    @Reference(dynamic = true, multiple = false, optional = true)
    public void setSocketIoBroadcasting(SocketIoBroadcasting socketIoBroadcasting) {
        this.socketIoBroadcastingRef.set(socketIoBroadcasting);
    }

    public void unsetSocketIoBroadcasting(SocketIoBroadcasting socketIoBroadcasting) {
        this.socketIoBroadcastingRef.compareAndSet(socketIoBroadcasting, null);
    }

    @Override
    public final void bundleChanged(final BundleEvent event) {
        if (event.getType() == BundleEvent.STOPPED) {
            sendBundleStoppedMessage(event);
        } else if (BundleEvent.STARTED == event.getType()) {
            sendBundleStartedMessage(event);
        }
    }

    private synchronized void sendBundleStoppedMessage(BundleEvent event) {
        String symbolicName = event.getBundle().getSymbolicName();
        if (socketIoBroadcastingRef.get() == null) {
            //log.debug("could not send bundleStoppedMessage for {}", symbolicName);
            return;
        }
        socketIoBroadcastingRef.get().send("bundle " + symbolicName + " was stopped");
    }

    private synchronized void sendBundleStartedMessage(BundleEvent event) {
        String symbolicName = event.getBundle().getSymbolicName();
        if (socketIoBroadcastingRef.get() == null) {
            //log.debug("could not send bundleStartedMessage for {}", symbolicName);
            return;
        }
        socketIoBroadcastingRef.get().send("bundle " + symbolicName + " was started");
    }

}
