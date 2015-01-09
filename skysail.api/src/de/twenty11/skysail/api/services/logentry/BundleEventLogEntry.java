package de.twenty11.skysail.api.services.logentry;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

@Slf4j
public class BundleEventLogEntry {

    private Date timestamp;
    private String bundle;
    private String type;
    private long origin;
    private Object source;
    private long bundleId;

    public BundleEventLogEntry(BundleEvent event) {
        log.info("BundleEvent '{}': Source: {}, Origin: {}, Bundle #{}", new Object[] {
                translate(event.getType()).trim(), event.getSource(), event.getOrigin(),
                event.getBundle().getBundleId() });
        timestamp = new Date();
        Bundle b = event.getBundle();
        bundleId = b.getBundleId();
        bundle = b.getSymbolicName() + "_" + b.getVersion() + " [" + b.getBundleId() + "]";
        type = translate(event.getType());
        origin = event.getOrigin().getBundleId();
        source = event.getSource();
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:sss");
        StringBuilder sb = new StringBuilder(sdf.format(timestamp)).append(": ").append(type).append(": ")
                .append(bundle).append(" - ");
        if (bundleId != origin) {
            sb.append(" - Origin:").append(origin);
        }
        sb.append(", Source: ").append(source);
        return sb.toString();
    }

    private String translate(int type) {
        switch (type) {
        case BundleEvent.INSTALLED:
            return "installed      ";
        case BundleEvent.LAZY_ACTIVATION:
            return "lazy_activation";
        case BundleEvent.RESOLVED:
            return "resolved       ";
        case BundleEvent.STARTED:
            return "started        ";
        case BundleEvent.STARTING:
            return "starting       ";
        case BundleEvent.STOPPED:
            return "stopped        ";
        case BundleEvent.STOPPING:
            return "stopping       ";
        case BundleEvent.UNINSTALLED:
            return "uninstalled    ";
        case BundleEvent.UNRESOLVED:
            return "unresolved     ";
        case BundleEvent.UPDATED:
            return "updated        ";
        default:
            return "unknown        ";
        }
    }

}
