package io.skysail.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

@Slf4j
public class BundleUtils {

    public static String readResource(Bundle bundle, String path) {
        URL url = bundle.getResource(path);
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            while(br.ready()){
                sb.append(br.readLine()).append("\n");
            }
            br.close();
        } catch (IOException e2) {
            log.error(e2.getMessage(), e2);
        }
        return sb.toString();    }

//    public static Path getPath(Bundle bundle, String path) {
//        try {
//            return Paths.get(bundle.getResource(path).toURI());
//        } catch (URISyntaxException e) {
//            log.error(e.getMessage(),e);
//            return null;
//        }
//    }

}
