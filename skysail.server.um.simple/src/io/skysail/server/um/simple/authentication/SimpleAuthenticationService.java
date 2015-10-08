package io.skysail.server.um.simple.authentication;

import io.skysail.api.um.*;
import io.skysail.server.um.security.shiro.SkysailCookieAuthenticator;
import io.skysail.server.um.simple.FileBasedUserManagementProvider;
import io.skysail.server.utils.PasswordUtils;

import java.io.*;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.security.Authenticator;

@Slf4j
public class SimpleAuthenticationService implements AuthenticationService {

    private FileBasedUserManagementProvider provider;

    public SimpleAuthenticationService(FileBasedUserManagementProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authenticator getAuthenticator(Context context) {
        CacheManager cacheManager = null;
        if (provider != null) {
            cacheManager = this.provider.getCacheManager();
        } else {
            log.info("no cacheManager available in {}", this.getClass().getName());
        }
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes(),
                cacheManager);
    }

    @Override
    public void clearCache(String username) {
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        validateUser(user);
        updateConfigFile(user, newPassword);
        clearCache(user.getUsername());
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        // app.clearCache(username);
    }

    private void updateConfigFile(User user, String newPassword) {
        String bCryptHash = PasswordUtils.createBCryptHash(newPassword);
        String fileInstallDir = System.getProperty("felix.fileinstall.dir");
        String configFileName = fileInstallDir + java.io.File.separator
                + FileBasedUserManagementProvider.class.getName() + ".cfg";
        Properties properties = new Properties();

        File configFile = new File(configFileName);
        try {
            properties.load(new FileReader(configFileName));
            properties.replace(user.getUsername() + ".password", bCryptHash);
            properties.store(new FileWriter(configFile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validateUser(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

}
