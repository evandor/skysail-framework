package io.skysail.server.um.security.shiro;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.util.SimpleByteSource;

import de.twenty11.skysail.server.utils.HashedPasswordAndSalt;
import de.twenty11.skysail.server.utils.PasswordUtils;

@Slf4j
public class SkysailHashedCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken userProvidedToken, AuthenticationInfo info) {
        SimpleByteSource salt = null;
        String saltAsString = null;
        if (info instanceof SaltedAuthenticationInfo) {
            salt = (SimpleByteSource) ((SaltedAuthenticationInfo) info).getCredentialsSalt();
            saltAsString = salt != null ? fromHex(salt.toHex()) : "";
        }
        String userProvidedPassword = new String((char[]) userProvidedToken.getCredentials());
        try {
            HashedPasswordAndSalt match = HashedPasswordAndSalt.direct(new String((char[]) info.getCredentials()),
                    saltAsString);
            return PasswordUtils.validate(userProvidedPassword, match.getHashedPassword());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private String fromHex(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    protected boolean hashProvidedCredentials(HashedPasswordAndSalt pah, String originalPassword) {
        try {
            return PasswordUtils.validate(originalPassword, pah);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
