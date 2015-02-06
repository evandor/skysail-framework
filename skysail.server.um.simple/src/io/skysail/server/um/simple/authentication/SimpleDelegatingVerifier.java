package io.skysail.server.um.simple.authentication;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.security.SecretVerifier;

@Slf4j
public class SimpleDelegatingVerifier extends SecretVerifier {

    @Override
    public int verify(String identifier, char[] secret) {
        identifier = identifier.replace("@", "&#64;");
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(identifier, new String(secret));
        log.info("login event for user '{}'", identifier);
        try {
            currentUser.login(token);
            log.info("login event for user '{}' successful", identifier);
            return RESULT_VALID;
        } catch (UnknownAccountException uae) {
            log.info("UnknownAccountException '{}' when login in {}", uae.getMessage(), identifier);
        } catch (IncorrectCredentialsException ice) {
            log.info("IncorrectCredentialsException '{}' when login in {}", ice.getMessage(), identifier);
        } catch (LockedAccountException lae) {
            log.info("LockedAccountException '{}' when login in {}", lae.getMessage(), identifier);
        } catch (AuthenticationException ae) {
            log.error("AuthenticationException '{}' when login in {}", ae.getMessage(), identifier);
        }
        return RESULT_INVALID;
    }

}
