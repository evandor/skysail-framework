package de.twenty11.skysail.server.utils.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.twenty11.skysail.server.utils.HashedPasswordAndSalt;

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
