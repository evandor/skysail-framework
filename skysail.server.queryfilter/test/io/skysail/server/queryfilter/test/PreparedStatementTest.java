package io.skysail.server.queryfilter.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.queryfilter.PreparedStatement;

import java.util.Arrays;

import org.junit.Test;

public class PreparedStatementTest {


    @Test
    public void simplest_statement_has_no_params_and_empty_sql() {
        PreparedStatement preparedStatement = new PreparedStatement();
        assertThat(preparedStatement.getParams().size(), is(0));
        assertThat(preparedStatement.getSql(), is(""));
    }
    
    @Test
    public void stringConstructor_adds_parameter_to_Sql() {
        PreparedStatement preparedStatement = new PreparedStatement("str");
        assertThat(preparedStatement.getParams().size(), is(0));
        assertThat(preparedStatement.getSql(), is("str"));
    }
    
    @Test
    public void and() {
        PreparedStatement firstStatement = new PreparedStatement("first");
        PreparedStatement secondStatement = new PreparedStatement("second");

        PreparedStatement outerStatement = new PreparedStatement("AND", Arrays.asList(firstStatement, secondStatement));
        
        assertThat(outerStatement.getParams().size(), is(0));
        assertThat(outerStatement.getSql(), is("first AND second"));
        
    }

    @Test
    public void and_with_parameter() {
        PreparedStatement firstStatement = new PreparedStatement("a=:a");
        firstStatement.put("a", "b");
        
        PreparedStatement secondStatement = new PreparedStatement("c=:c");
        secondStatement.put("c", "d");
        
        PreparedStatement outerStatement = new PreparedStatement("AND", Arrays.asList(firstStatement, secondStatement));
        
        assertThat(outerStatement.getParams().size(), is(2));
        assertThat(outerStatement.getSql(), is("a=:a AND c=:c"));
        
    }

}
