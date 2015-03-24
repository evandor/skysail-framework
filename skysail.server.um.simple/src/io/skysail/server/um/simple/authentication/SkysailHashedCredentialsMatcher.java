package io.skysail.server.um.simple.authentication;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import de.twenty11.skysail.server.utils.HashedPasswordAndSalt;
import de.twenty11.skysail.server.utils.PasswordUtils;

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
        if (cacheManager != null) {
            Cache<Object, Object> cache = cacheManager.getCache(CREDENTIALS_CACHE);
            log.info("checking cache for entry {}", match.getHashedPassword());
            Object object = cache.get(match.getHashedPassword());
            if (object != null && object instanceof LocalDateTime) {
                LocalDateTime timestamp = (LocalDateTime) object;
                if (Duration.between(timestamp, LocalDateTime.now()).toMinutes() < 5) {
                    log.info("found entry in credentials hash cache, updating timestamp");
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
        if (validated && cacheManager != null) {
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