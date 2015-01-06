package de.twenty11.skysail.server.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static String convertStreamToString(java.io.InputStream is) {
        if (is == null) {
            return null;
        }
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        s.close();
        return result;
    }

    public static Map<String, String> readSecurityDefinitions(String securityDefinitions) {
        Map<String, String> result = new HashMap<String, String>();
        for (String line : securityDefinitions.split("\\n")) {
            String[] assignment = line.split("=");
            if (assignment.length != 2) {
                logger.error("cannot deal with line '{}' in security.ini", line);
            }
            String path = assignment[0].trim();
            String filters = assignment[1].trim();
            result.put(path, filters);
        }
        return result;
    }

}
