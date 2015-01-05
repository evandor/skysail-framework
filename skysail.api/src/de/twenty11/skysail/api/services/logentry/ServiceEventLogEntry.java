package de.twenty11.skysail.api.services.logentry;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

public class ServiceEventLogEntry {

	private Date timestamp;
	private String bundle;
	private String type;
	private Object source;
	private String usingBundles = "";
	private String properties = "";

	public ServiceEventLogEntry(ServiceEvent event) {
		timestamp = new Date();
		ServiceReference<?> sr = event.getServiceReference();
		Bundle b = sr.getBundle();
		bundle = b.getSymbolicName() + "_" + b.getVersion() + " [" + b.getBundleId() + "]";

		if (sr.getUsingBundles() != null) {
			usingBundles = Arrays.asList(sr.getUsingBundles()).stream().map(t -> Long.toString(t.getBundleId()))
			        .collect(Collectors.joining(","));
		}
		if (sr.getPropertyKeys() != null) {
			properties = Arrays.asList(sr.getPropertyKeys()).stream().map(key -> keyValue(sr, key))
			        .collect(Collectors.joining(","));
		}
		type = translate(event.getType());
		source = event.getSource();
	}

	private String keyValue(ServiceReference<?> sr, String key) {
		return new StringBuilder(key).append(" -> ").append(sr.getProperty(key)).toString();
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:sss");
		StringBuilder sb = new StringBuilder(sdf.format(timestamp)).append(": ").append(type).append(": ")
		        .append(bundle).append(" - ");
		sb.append("UsingBundles: ").append(usingBundles);
		sb.append("Properties: ").append(properties);
		sb.append(", Source: ").append(source);
		return sb.toString();
	}

	private String translate(int type) {
		switch (type) {
		case ServiceEvent.MODIFIED:
			return "modified";
		case ServiceEvent.MODIFIED_ENDMATCH:
			return "modified";
		case ServiceEvent.REGISTERED:
			return "modified";
		case ServiceEvent.UNREGISTERING:
			return "modified";
		default:
			return "unknown service type '" + type + "'";
		}
	}

}
