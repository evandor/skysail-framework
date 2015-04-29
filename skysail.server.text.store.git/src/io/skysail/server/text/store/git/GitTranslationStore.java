package io.skysail.server.text.store.git;

import io.skysail.api.text.TranslationStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.BundleContext;
import org.restlet.Request;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "=50" })
@Slf4j
public class GitTranslationStore implements TranslationStore {

    private static final String GIT_PATH_IDENTIFIER = "repository.path";

    private static final int KEY_SEGMENTS_LIMIT = 5;

    private String translationRepositoryPath = ".";

    @Activate
    public void activate(Map<String, String> config) {
        if (config.get(GIT_PATH_IDENTIFIER) != null) {
            translationRepositoryPath = config.get(GIT_PATH_IDENTIFIER);
        }
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(translate(key, new Locale("en")));
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request) {
        return get(key);
    }

    @Override
    public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {
        return get(key);
    }

    private String translate(String key, Locale locale) {
        Properties props = loadProperties(getFile(key, locale));
        return props == null ? null : props.getProperty(key);
    }

    private Properties loadProperties(String filePath) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(filePath));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
        return props;
    }

    @Override
    public boolean persist(String key, String message, Locale locale, BundleContext bc) {
        String path = getFile(key, locale);
        File file = new File(path);
        if (!file.exists()) {
            create(file, locale);
        }
        
        Properties props = loadProperties(getFile(key, locale));
        props.put(key, message);
        
        @SuppressWarnings("serial")
        Properties tmp = new Properties() {
            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(new TreeSet<Object>(super.keySet()));
            }
        };
        tmp.putAll(props);
        try {
            tmp.store(new FileWriter(file), null);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        
        return false;
    }

    private void create(File file, Locale locale) {
        new File(getPath(locale)).mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("problem creating file '{}':", file, e.getMessage(), e);
        }
    }

    private String getFile(String key, Locale locale) {
        String[] split = key.split("\\.");
        String fileIdentifier = Arrays.stream(split).limit(KEY_SEGMENTS_LIMIT).collect(Collectors.joining("."));
        return getPath(locale) + File.separator + fileIdentifier + ".properties";
    }

    private String getPath(Locale locale) {
        return translationRepositoryPath + File.separator + locale.toString();
    }

}
