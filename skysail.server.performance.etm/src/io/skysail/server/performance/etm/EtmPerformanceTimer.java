package io.skysail.server.performance.etm;

import io.skysail.server.services.PerformanceTimer;
import etm.core.monitor.EtmPoint;

public class EtmPerformanceTimer implements PerformanceTimer {

    private EtmPoint point;

    public EtmPerformanceTimer(EtmPoint point) {
        this.point = point;
    }

    @Override
    public void stop() {
        point.collect();
    }

}
