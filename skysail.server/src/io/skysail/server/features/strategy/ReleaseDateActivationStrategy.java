package io.skysail.server.features.strategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

import de.twenty11.skysail.api.features.ActivationStrategy;
import de.twenty11.skysail.api.features.FeatureState;
import de.twenty11.skysail.api.um.SkysailUser;

@Slf4j
public class ReleaseDateActivationStrategy implements ActivationStrategy {

    public static final String ID = "release-date";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_TIME = "time";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Release date";
    }

    @Override
    public boolean isActive(FeatureState featureState, SkysailUser user) {
        String dateStr = featureState.getConfig().get(PARAM_DATE);
        if (dateStr == null || dateStr.trim().length() == 0) {
            return false;
        }
        String timeStr = featureState.getConfig().get(PARAM_TIME);
        return releaseDateBeforeNow(dateStr, timeStr);
    }

    private boolean releaseDateBeforeNow(String dateStr, String timeStr) {
        Date parsedReleaseDate = parseReleaseDate(dateStr, timeStr);
        if (parsedReleaseDate == null) {
            return false;
        }
        return new Date().after(parsedReleaseDate);
    }

    private Date parseReleaseDate(String dateStr, String timeStr) {
        StringBuilder fullDate = new StringBuilder();
        fullDate.append(dateStr.trim());
        fullDate.append('T');
        if (StringUtils.isNotBlank(timeStr)) {
            fullDate.append(timeStr.trim());
        } else {
            fullDate.append("00:00:00");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            return dateFormat.parse(fullDate.toString());
        } catch (ParseException e) {
            log.error("Invalid date and/or time: " + fullDate.toString());
        }
        return null;
    }

}
