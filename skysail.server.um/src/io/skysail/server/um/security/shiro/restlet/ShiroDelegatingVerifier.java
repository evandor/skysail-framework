package io.skysail.server.um.security.shiro.restlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.security.SecretVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroDelegatingVerifier extends SecretVerifier {

    private static final Logger logger = LoggerFactory.getLogger(ShiroDelegatingVerifier.class);

    @Override
    public int verify(String identifier, char[] secret) {
        // TODO rethink special chars / escaping
        identifier = identifier.replace("@", "&#64;");
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(identifier, new String(secret));
        logger.info("login event for user '{}'", identifier);
        try {
            currentUser.login(token);
            logger.info("login event for user '{}' successful", identifier);
            return RESULT_VALID;
        } catch (UnknownAccountException uae) {
            logger.info("UnknownAccountException '{}' when login in {}", uae.getMessage(), identifier);
        } catch (IncorrectCredentialsException ice) {
            logger.info("IncorrectCredentialsException '{}' when login in {}", ice.getMessage(), identifier);
        } catch (LockedAccountException lae) {
            logger.info("LockedAccountException '{}' when login in {}", lae.getMessage(), identifier);
        } catch (AuthenticationException ae) {
            logger.error("AuthenticationException '{}' when login in {}", ae.getMessage(), identifier);
        }
        return RESULT_INVALID;
    }
}
