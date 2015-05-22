package io.skysail.server.queryfilter.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.queryfilter.Filter;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class FilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void integer_as_filter_expression_is_valid() {
        Filter Filter = new Filter("17");
        assertThat(Filter.isValid(),is(true));
    }

//    @Test
//    public void null_as_filter_expression_throws_exception() {
//        thrown.expect(NullPointerException.class);
//        new Filter(null);
//    }

    @Test
    public void empty_filter_expression_is_not_valid() {
        Filter Filter = new Filter("");
        assertThat(Filter.isValid(),is(false));
    }

    @Test
    public void simple_filter_expression_is_valid() {
        Filter Filter = new Filter("(a=b)");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getLdapFilter().isLeaf(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("a=b"));
    }
    
    @Test
    public void and_filter_expression_is_valid() {
        Filter Filter = new Filter("(&(a=b)(c=d))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("a=b AND c=d"));
    }

    @Test
    public void and_filter_expression_with_three_arguments_is_valid() {
        Filter Filter = new Filter("(&(a=b)(c=d)(e=f))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("a=b AND c=d AND e=f"));
    }

    @Test
    public void or_filter_expression_is_valid() {
        Filter Filter = new Filter("(|(a=b)(c=d))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("a=b OR c=d"));
    }
    
    @Test
    public void wildcard_filter_expression_is_valid() {
        Filter Filter = new Filter("(a=*b*))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("a LIKE '%b%'"));
    }
    
    @Test
    public void not_filter_expression_is_valid() {
        Filter Filter = new Filter("(!(a=b))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getSqlExpression(),equalTo("NOT a=b"));
    }

}
