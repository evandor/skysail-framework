
package io.skysail.server.db.versions.impl.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.versions.impl.Migration;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class MigrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void null_path_gives_Exception() {
        thrown.expect(NullPointerException.class);
        new Migration(null,"content");
    }

    @Test
    public void path_not_starting_with_V_gives_Exception() {
        thrown.expect(IllegalArgumentException.class);
        new Migration("xxx","content");
    }

    @Test
    public void path_not_ending_with_sqlPostfix_gives_Exception() {
        thrown.expect(IllegalArgumentException.class);
        new Migration("V.txt","content");
    }

    @Test
    public void valid_path_must_have_Integer_after_V() {
        thrown.expect(IllegalArgumentException.class);
        new Migration("Vx.sql","content");
    }

    @Test
    public void valid_path_must_contain_underscore() {
        thrown.expect(IllegalArgumentException.class);
        new Migration("V1.sql","content");
    }

    @Test
    public void leading_zeros_are_ignored() {
        assertThat(new Migration("V01_xxx.sql","content").getVersion(), is(1));
    }

    @Test
    public void VO_is_valid() {
        new Migration("V0_xxx.sql","content");
    }

    @Test
    public void V3_xxx_has_version_of_3() {
        assertThat(new Migration("V3_xxx.sql","content").getVersion(), is(3));
    }

    @Test
    @Ignore
    public void V3_xxx_has_title_of_xxx() {
        assertThat(new Migration("V3_xxx.sql","content").getTitle(), is("xxx"));
    }

    @Test
    public void V123_abc_def_has_version_of_3() {
        assertThat(new Migration("V123_abc_def.sql","content").getVersion(), is(123));
    }

    @Test
    @Ignore

    public void V123_abc_def_has_title_of_abc_def() {
        assertThat(new Migration("V123_abc_def.sql","content").getTitle(), is("abc_def"));
    }

}
