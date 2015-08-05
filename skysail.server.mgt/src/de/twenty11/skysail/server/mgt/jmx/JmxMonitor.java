package de.twenty11.skysail.server.mgt.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JmxMonitor {

    MemoryMXBean muBean = ManagementFactory.getMemoryMXBean();
    MemoryMXBean muNHBean = ManagementFactory.getMemoryMXBean();

    public class JmxMonitorTask extends TimerTask {

        private volatile List<MemorySnapshot> muList = new ArrayList<MemorySnapshot>();
        private volatile List<MemorySnapshot> muNhList = new ArrayList<MemorySnapshot>();

        @Override
        public void run() {
            // http://stackoverflow.com/questions/10999076/programmatically-print-the-heap-usage-that-is-typically-printed-on-jvm-exit-when?lq=1
            long now = System.currentTimeMillis();
            logMemoryUsage(now);
        }

        private void logMemoryUsage(long now) {
            muList.add(new MemorySnapshot(now, muBean.getHeapMemoryUsage()));
            muNhList.add(new MemorySnapshot(now, muNHBean.getNonHeapMemoryUsage()));
        }

    }

    private static final long INTERVAL = 1000 * 10;
    private Timer timer;
    private JmxMonitorTask checkMemoryTask;

    public void start() {
        checkMemoryTask = new JmxMonitorTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(checkMemoryTask, new Date(new Date().getTime() + 10 * 1000), INTERVAL);
    }

    public List<MemorySnapshot> getHeapStats() {
        return checkMemoryTask.muList;
    }

    public List<MemorySnapshot> getNonHeapStats() {
        return checkMemoryTask.muNhList;
    }

    public void stop() {
        timer.cancel();
    }

}
