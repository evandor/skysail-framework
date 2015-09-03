package io.skysail.server.db;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@ToString(of = { "name", "driver", "url", "username" })
public class DbConfig {

    public static final String NAME = "name";
    public static final String DRIVER = "driver";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Getter
    private String name;

    @Getter
    private String driver;

    @Getter
    private String url;

    @Getter
    private String username;

    @Getter
    private String password;

    private Map<String, String> configMap;

    public DbConfig(Map<String, String> config) {
        this.name = config.getOrDefault(NAME, "defaultname");
        this.driver = config.getOrDefault(DRIVER, "");
        this.url = config.getOrDefault(URL, "");
        this.username = config.getOrDefault(USERNAME, "");
        this.password = config.getOrDefault(PASSWORD, "");
        this.configMap = config;
    }

    public String get(String key) {
        return configMap.get(key);
    }

}
