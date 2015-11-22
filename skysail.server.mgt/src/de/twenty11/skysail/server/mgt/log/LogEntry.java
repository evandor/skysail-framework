package de.twenty11.skysail.server.mgt.log;

import io.skysail.api.domain.Identifiable;

import java.text.SimpleDateFormat;

import lombok.*;

import org.osgi.service.log.LogService;

@Getter
public class LogEntry implements Identifiable {

    @Setter
    private String id;
    private String msg;
    private String level;
    private String time;
    private org.osgi.service.log.LogEntry originalEntry;

    public LogEntry(String msg) {
        this.msg = msg;
    }

    public LogEntry(org.osgi.service.log.LogEntry entry) {
        this.level = format(entry.getLevel());
        this.time = formatTime(entry.getTime());
        this.msg = entry.getMessage().replace("\n", "<br>").replace("'", "&#39;");
        this.originalEntry = entry;
    }

    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(timestamp);
    }

    public boolean isDebug() {
        return originalEntry.getLevel() == LogService.LOG_DEBUG;
    }

    public boolean isWarning() {
        return originalEntry.getLevel() == LogService.LOG_WARNING;
    }

    public boolean isError() {
        return originalEntry.getLevel() == LogService.LOG_ERROR;
    }

    public boolean isInfo() {
        return originalEntry.getLevel() == LogService.LOG_INFO;
    }

    private String format(int level) {
        switch (level) {
        case LogService.LOG_DEBUG:
            return "[debug]";
        case LogService.LOG_ERROR:
            return "[error]";
        case LogService.LOG_INFO:
            return "[info]";
        case LogService.LOG_WARNING:
            return "[warning]";
        default:
            return "unknown";
        }
    }

}
