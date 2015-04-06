package io.skysail.server.utils;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.utils.HashedPasswordAndSalt;
import io.skysail.server.utils.PasswordUtils;

import org.junit.Ignore;
import org.junit.Test;

public class PasswordUtilsTest {

    @Test
    @Ignore
    public void iterations_is_big_enough() {
        assertThat(PasswordUtils.ITERATIONS, is(greaterThanOrEqualTo(1000)));
    }

    @Test
    public void creates_hash_and_salt() throws Exception {
        HashedPasswordAndSalt createHash = PasswordUtils.createHashAndSalt("password");
        assertThat(createHash.getHashedPassword().length(), is(greaterThanOrEqualTo(128)));
        assertThat(createHash.getSalt().length(), is(greaterThan(16)));
    }

    @Test
    public void can_validate_password_against_created_hash_and_salt() throws Exception {
        // salt and hashed password will be saved, e.g. in a db
        HashedPasswordAndSalt hashedPasswordAndSalt = PasswordUtils.createHashAndSalt("skysail");

        // now user logs in again, providing plain text password, which is combined with personal salt from db
        boolean valid = PasswordUtils.validate("skysail", hashedPasswordAndSalt);
        assertThat(valid, is(true));
    }

    @Test
    public void can_reject_password_against_created_hash_and_salt() throws Exception {
        HashedPasswordAndSalt hashedPasswordAndSalt = PasswordUtils.createHashAndSalt("password");
        boolean valid = PasswordUtils.validate("password_", hashedPasswordAndSalt);
        assertThat(valid, is(false));
    }
}
