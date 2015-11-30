package io.skysail.server.performance.etm;

import org.osgi.service.component.annotations.*;

import etm.core.configuration.*;
import etm.core.monitor.EtmMonitor;
import io.skysail.server.services.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Getter
@Slf4j
public class EtmPerformanceMonitor  implements PerformanceMonitor {
    
    private static EtmMonitor etmMonitor = null;
    
    @Activate
    public void activate() {
        log.debug("starting etm performance monitor");
        BasicEtmConfigurator.configure();
        etmMonitor = EtmManager.getEtmMonitor();
        etmMonitor.start();
    }

    @Deactivate
    public void deactivate() {
        log.debug("stopping etm performance monitor");
        etmMonitor.stop();
        etmMonitor = null;
    }

    @Override
    public PerformanceTimer start(String identifier) {
        if (etmMonitor == null) {
            log.debug("etm performance monitor is not available");
            return null;
        }
        return new EtmPerformanceTimer(etmMonitor.createPoint(identifier));
    }
    
    public static EtmMonitor getEtmMonitor() {
        return etmMonitor;
    }

}
