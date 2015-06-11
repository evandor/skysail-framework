package io.skysail.server.um.simple.authentication;

import io.skysail.server.utils.*;

import java.time.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.*;

import de.twenty11.skysail.server.core.restlet.SecurityFeatures;

/**
 * A CredentialsMatcher delegating to the PasswordUtils, and adding caching of
 * successful logins.
 * 
 *
 */
@Slf4j
public class SkysailHashedCredentialsMatcher extends SimpleCredentialsMatcher {

    public static final String CREDENTIALS_CACHE = "credentialsCache";

    private CacheManager cacheManager;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken userProvidedToken, AuthenticationInfo info) {
        String userProvidedPassword = new String((char[]) userProvidedToken.getCredentials());
        HashedPasswordAndSalt match = HashedPasswordAndSalt.direct(new String((char[]) info.getCredentials()), null);
        if (cacheManager != null && SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE.isActive()) {
            Cache<Object, Object> cache = cacheManager.getCache(CREDENTIALS_CACHE);
            log.debug("checking cache for entry {}", match.getHashedPassword());
            Object object = cache.get(match.getHashedPassword());
            if (object != null && object instanceof LocalDateTime) {
                LocalDateTime timestamp = (LocalDateTime) object;
                if (Duration.between(timestamp, LocalDateTime.now()).toMinutes() < 5) {
                    log.debug("found entry in credentials hash cache, updating timestamp");
                    cache.put(match.getHashedPassword(), LocalDateTime.now());
                    return true;
                }
            }
        }
        return valiateAndCacheIfOk(info, userProvidedPassword, null);
    }

    public void setCacheManager(CacheManager cacheManager) {
        log.debug("cacheManager set in {}", this.getClass().getName());
        this.cacheManager = cacheManager;
    }

    public long getCacheSize() {
        return cacheManager.getCache(CREDENTIALS_CACHE).size();
    }

    private boolean valiateAndCacheIfOk(AuthenticationInfo info, String userProvidedPassword, String saltAsString) {
        try {
            HashedPasswordAndSalt match = HashedPasswordAndSalt.direct(new String((char[]) info.getCredentials()),
                    saltAsString);
            String hashedPassword = match.getHashedPassword();
            boolean validated = PasswordUtils.validate(userProvidedPassword, hashedPassword);
            addToCache(hashedPassword, validated);
            return validated;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private void addToCache(String hash, boolean validated) {
        if (validated && cacheManager != null && SecurityFeatures.USE_CREDENTIALS_CACHE_FEATURE.isActive()) {
            Cache<Object, Object> cache = cacheManager.getCache(CREDENTIALS_CACHE);
            cache.put(hash, LocalDateTime.now());
            log.info("added entry {} to credentials hash cache with current timestamp, size now is {}", hash,
                    cache.size());
        }
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