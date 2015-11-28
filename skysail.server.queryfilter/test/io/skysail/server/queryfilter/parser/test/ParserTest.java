package io.skysail.server.queryfilter.parser.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.queryfilter.*;
import io.skysail.server.queryfilter.nodes.*;
import io.skysail.server.queryfilter.parser.Parser;

import org.junit.*;
import org.osgi.framework.InvalidSyntaxException;

public class ParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void equality() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(status=ARCHIVED)").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.EQUAL)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void isNotNull() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(status=*)").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.PRESENT)));
        assertThat(parsed.isLeaf(), is(true));
    }

    @Test
    public void isNull() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(!(status=*))").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.NOT)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void not() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(!(status=ARCHIVED))").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.NOT)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void and() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(&(status=ARCHIVED)(user=admin))").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.AND)));
        assertThat(parsed.isLeaf(), is(false));
    }


    @Test
    public void or() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(|(status=ARCHIVED)(user=admin))").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.OR)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void in() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(#17:0 e out['parent'])").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.IN)));
        assertThat(parsed.isLeaf(), is(true));
        assertThat(parsed, is(instanceOf(IsInNode.class)));
        assertThat(((IsInNode)parsed).getValue(),is("out['parent']"));
    }

    @Test
    public void less() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(due < date[])").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.LESS)));
        assertThat(parsed.isLeaf(), is(true));
        assertThat(parsed, is(instanceOf(LessNode.class)));
        assertThat(((LessNode)parsed).getValue(),is("date[]"));
    }

    @Test
    public void complex_less() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(&(due < date())(!(status=ARCHIVED)))").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.AND)));
        assertThat(parsed.isLeaf(), is(false));
    }

    @Test
    public void greater() throws InvalidSyntaxException {
        ExprNode parsed = new Parser("(due > date())").parse();
        assertThat(parsed.getOperation(), is(equalTo(Operation.GREATER)));
        assertThat(parsed.isLeaf(), is(true));
    }

}
