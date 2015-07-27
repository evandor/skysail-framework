package io.skysail.server.queryfilter.parser;

import io.skysail.server.queryfilter.ExprNode;
import io.skysail.server.queryfilter.nodes.*;

import java.util.*;

import org.osgi.framework.InvalidSyntaxException;

/**
 * @author org.osgi.framework.FrameworkUtil.ExprNode.Parser
 *
 */
public class Parser {

    private final String filterstring;
    private final char[] filterChars;
    private int pos;

    public Parser(String filterstring) {
        this.filterstring = filterstring;
        filterChars = filterstring.toCharArray();
        pos = 0;
    }

    public ExprNode parse() throws InvalidSyntaxException {
        ExprNode filter;
        try {
            filter = parse_filter();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidSyntaxException("Filter ended abruptly", filterstring, e);
        }

        if (pos != filterChars.length) {
            throw new InvalidSyntaxException("Extraneous trailing characters: " + filterstring.substring(pos),
                    filterstring);
        }
        return filter;
    }

    private ExprNode parse_filter() throws InvalidSyntaxException {
        skipWhiteSpace();
        if (filterChars[pos] != '(') {
            throw new InvalidSyntaxException("Missing '(': " + filterstring.substring(pos), filterstring);
        }
        pos++;
        ExprNode filter = parse_filtercomp();
        skipWhiteSpace();
        if (filterChars[pos] != ')') {
            throw new InvalidSyntaxException("Missing ')': " + filterstring.substring(pos), filterstring);
        }
        pos++;
        skipWhiteSpace();
        return filter;
    }

    private ExprNode parse_filtercomp() throws InvalidSyntaxException {
        skipWhiteSpace();

        char c = filterChars[pos];

        switch (c) {
        case '&': {
            pos++;
            return parse_and();
        }
        case '|': {
            pos++;
            return parse_or();
        }
        case '!': {
            pos++;
            return parse_not();
        }
        }
        return parse_item();
    }

    private ExprNode parse_and() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parse_item();
        }

        List<ExprNode> operands = new ArrayList<ExprNode>(10);

        while (filterChars[pos] == '(') {
            ExprNode child = parse_filter();
            operands.add(child);
        }

        return new AndNode(operands);
    }

    private ExprNode parse_or() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parse_item();
        }

        List<ExprNode> operands = new ArrayList<ExprNode>(10);

        while (filterChars[pos] == '(') {
            ExprNode child = parse_filter();
            operands.add(child);
        }

        return new OrNode(operands);
    }

    private ExprNode parse_not() throws InvalidSyntaxException {
        int lookahead = pos;
        skipWhiteSpace();

        if (filterChars[pos] != '(') {
            pos = lookahead - 1;
            return parse_item();
        }

        ExprNode child = parse_filter();

        return new NotNode(child);
    }

    private ExprNode parse_item() throws InvalidSyntaxException {
        String attr = parse_attr();

        skipWhiteSpace();

        switch (filterChars[pos]) {
        case '~': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                return null;// new ExprNode(Operation.APPROX, attr,
                            // parse_value());
            }
            break;
        }
        case '>': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                return null;// new ExprNode(Operation.GREATER, attr,
                            // parse_value());
            }
            break;
        }
        case '<': {
            if (filterChars[pos + 1] == '=') {
                pos += 2;
                return null;// new ExprNode(Operation.LESS, attr,
                            // parse_value());
            }
            break;
        }
        case '=': {
            if (filterChars[pos + 1] == '*') {
                int oldpos = pos;
                pos += 2;
                skipWhiteSpace();
                if (filterChars[pos] == ')') {
                    return null;// new ExprNode(Operation.PRESENT, attr, null);
                }
                pos = oldpos;
            }

            pos++;
            Object string = parse_substring();

            if (string instanceof String) {
                return new EqualityNode(attr, (String) string);
            }
            return null;// new ExprNode(Operation.SUBSTRING, attr, string);
        }
        case '∈': { // "element of", "is in" \u2208, not standard LDAP syntax!
            pos++;
            Object string = parse_substring();

            if (string instanceof String) {
                return new IsInNode(attr, (String) string);
            }
            return null;
        }
        }

        throw new InvalidSyntaxException("Invalid operator: " + filterstring.substring(pos), filterstring);
    }

    private String parse_attr() throws InvalidSyntaxException {
        skipWhiteSpace();

        int begin = pos;
        int end = pos;

        char c = filterChars[pos];

        while (c != '~' && c != '∈' && c != '<' && c != '>' && c != '=' && c != '(' && c != ')') {
            pos++;

            if (!Character.isWhitespace(c)) {
                end = pos;
            }

            c = filterChars[pos];
        }

        int length = end - begin;

        if (length == 0) {
            throw new InvalidSyntaxException("Missing attr: " + filterstring.substring(pos), filterstring);
        }

        return new String(filterChars, begin, length);
    }

//    private String parse_value() throws InvalidSyntaxException {
//        StringBuffer sb = new StringBuffer(filterChars.length - pos);
//
//        parseloop: while (true) {
//            char c = filterChars[pos];
//
//            switch (c) {
//            case ')': {
//                break parseloop;
//            }
//
//            case '(': {
//                throw new InvalidSyntaxException("Invalid value: " + filterstring.substring(pos), filterstring);
//            }
//
//            case '\\': {
//                pos++;
//                c = filterChars[pos];
//                /* fall through into default */
//            }
//
//            default: {
//                sb.append(c);
//                pos++;
//                break;
//            }
//            }
//        }
//
//        if (sb.length() == 0) {
//            throw new InvalidSyntaxException("Missing value: " + filterstring.substring(pos), filterstring);
//        }
//
//        return sb.toString();
//    }

    private Object parse_substring() throws InvalidSyntaxException {
        StringBuffer sb = new StringBuffer(filterChars.length - pos);

        List<String> operands = new ArrayList<String>(10);

        parseloop: while (true) {
            char c = filterChars[pos];

            switch (c) {
            case ')': {
                if (sb.length() > 0) {
                    operands.add(sb.toString());
                }

                break parseloop;
            }

            case '(': {
                throw new InvalidSyntaxException("Invalid value: " + filterstring.substring(pos), filterstring);
            }

            case '*': {
                if (sb.length() > 0) {
                    operands.add(sb.toString());
                }

                sb.setLength(0);

                operands.add(null);
                pos++;

                break;
            }

            case '\\': {
                pos++;
                c = filterChars[pos];
                /* fall through into default */
            }

            default: {
                sb.append(c);
                pos++;
                break;
            }
            }
        }

        int size = operands.size();

        if (size == 0) {
            return "";
        }

        if (size == 1) {
            Object single = operands.get(0);

            if (single != null) {
                return single;
            }
        }

        return operands.toArray(new String[size]);
    }

    private void skipWhiteSpace() {
        for (int length = filterChars.length; (pos < length) && Character.isWhitespace(filterChars[pos]);) {
            pos++;
        }
    }

}
