package io.skysail.server.um.simple.authentication;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.User;
import io.skysail.server.um.simple.SimpleUserManagementProvider;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.security.Authenticator;

import de.twenty11.skysail.server.utils.PasswordUtils;

public class SimpleAuthenticationService implements AuthenticationService {

    @Override
    public Authenticator getAuthenticator(Context context) {
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes());
    }

    @Override
    public void clearCache(String username) {
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        validateUser(user);
        updateConfigFile(user, newPassword);
        clearCache(user.getUsername());
        // currentUser.setPassword(bCryptHash);
        // app.getUserManager().update(currentUser);
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        // app.clearCache(username);
    }

    private void updateConfigFile(User user, String newPassword) {
        String bCryptHash = PasswordUtils.createBCryptHash(newPassword);
        String fileInstallDir = System.getProperty("felix.fileinstall.dir");
        String configFileName = fileInstallDir + java.io.File.separator + SimpleUserManagementProvider.class.getName()
                + ".cfg";
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
