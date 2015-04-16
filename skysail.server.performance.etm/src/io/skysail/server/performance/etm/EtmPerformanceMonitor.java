package io.skysail.server.performance.etm;

import io.skysail.server.services.PerformanceMonitor;
import io.skysail.server.services.PerformanceTimer;
import aQute.bnd.annotation.component.Component;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;

@Component(immediate = true)
public class EtmPerformanceMonitor  implements PerformanceMonitor {
    
    protected static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

    @Override
    public PerformanceTimer start(String identifier) {
        return new EtmPerformanceTimer(etmMonitor.createPoint(identifier));
    }

}
