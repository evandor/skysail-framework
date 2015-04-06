package io.skysail.server.utils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.utils.HashedPasswordAndSalt;

import org.junit.Test;

public class HashedPasswordAndSaltTest {

    private HashedPasswordAndSalt passwordAndHash;

    @Test
    public void returns_salt_from_pah_created_with_strings() {
        passwordAndHash = HashedPasswordAndSalt.direct("hashedPassword", "salt");
        assertThat(passwordAndHash.getSalt(), is(equalTo("salt")));
    }

    @Test
    public void returns_hashedPassword_from_pah_created_with_strings() {
        passwordAndHash = HashedPasswordAndSalt.direct("hashedPassword", "salt");
        assertThat(passwordAndHash.getHashedPassword(), is(equalTo("hashedPassword")));
    }

}
