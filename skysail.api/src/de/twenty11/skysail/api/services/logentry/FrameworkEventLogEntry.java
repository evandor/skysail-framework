package de.twenty11.skysail.api.services.logentry;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkEvent;

public class FrameworkEventLogEntry {
	
	private Date timestamp;
	private String bundle;
	private String type;
	private Object source;

	public FrameworkEventLogEntry(FrameworkEvent event) {
		timestamp = new Date();
		Bundle b = event.getBundle();
		bundle = b.getSymbolicName() + "_" + b.getVersion() + " [" + b.getBundleId() + "]";
		type = translate(event.getType());
		source = event.getSource();
    }

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:sss");
		StringBuilder sb = new StringBuilder(sdf.format(timestamp)).append(": ").append(type).append(": ")
		        .append(bundle).append(" - ");
		sb.append(", Source: ").append(source);
		return sb.toString();
	}
	
	private String translate(int type) {
		switch (type) {
        case FrameworkEvent.ERROR:
        	return "ERROR             ";
        case FrameworkEvent.INFO:
        	return "INFO              ";
        case FrameworkEvent.PACKAGES_REFRESHED:
        	return "PACKAGES_REFRESHED";
        case FrameworkEvent.STARTED:
        	return "STARTED           ";
        case FrameworkEvent.STARTLEVEL_CHANGED:
        	return "STARTLEVEL_CHANGED";
        case FrameworkEvent.STOPPED:
        	return "STOPPED           ";
        case FrameworkEvent.STOPPED_BOOTCLASSPATH_MODIFIED:
        	return "STOPPED_BOOTCLASSPATH_MODIFIED";
        case FrameworkEvent.STOPPED_UPDATE:
        	return "STOPPED_UPDATE    ";
        case FrameworkEvent.WAIT_TIMEDOUT:
        	return "WAIT_TIMEDOUT     ";
        case FrameworkEvent.WARNING:
        	return "WARNING           ";
        default:
        	return "unknown event type '"+type+"'";
        }
    }

}
