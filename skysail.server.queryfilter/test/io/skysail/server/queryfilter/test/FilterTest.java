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

    @Test
    @Ignore
    public void empty_filter_expression_is_not_valid() {
        Filter Filter = new Filter("");
        assertThat(Filter.isValid(),is(false));
    }

    @Test
    public void simple_filter_expression_is_valid() {
        Filter filter = new Filter("(a=b)");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("a=:a"));
        assertThat(filter.getParams().size(),is(1));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));
    }
    
    @Test
    public void and_filter_expression_is_valid() {
        Filter filter = new Filter("(&(a=b)(c=d))");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("a=:a AND c=:c"));
        assertThat(filter.getParams().size(),is(2));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));
        assertThat(filter.getParams().get("c"),is(equalTo("d")));
    }

    @Test
    public void and_filter_expression_with_three_arguments_is_valid() {
        Filter Filter = new Filter("(&(a=b)(c=d)(e=f))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a=:a AND c=:c AND e=:e"));
    }

    @Test
    public void or_filter_expression_is_valid() {
        Filter Filter = new Filter("(|(a=b)(c=d))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a=:a OR c=:c"));
    }
    
    @Test
    @Ignore // not implemented yet
    public void wildcard_filter_expression_is_valid() {
        Filter Filter = new Filter("(a=*b*))");
        assertThat(Filter.isValid(),is(true));
        assertThat(Filter.getPreparedStatement(),equalTo("a LIKE '%b%'"));
    }
    
    @Test
    public void not_filter_expression_is_valid() {
        Filter filter = new Filter("(!(a=b))");
        assertThat(filter.isValid(),is(true));
        assertThat(filter.getPreparedStatement(),equalTo("NOT (a=:a)"));
        assertThat(filter.getParams().size(),is(1));
        assertThat(filter.getParams().get("a"),is(equalTo("b")));

    }

}
