package io.skysail.server.queryfilter;

import io.skysail.server.queryfilter.nodes.*;
import io.skysail.server.queryfilter.parser.Parser;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.InvalidSyntaxException;
import org.restlet.Request;

@Getter
@NoArgsConstructor
@Slf4j
@ToString(of = {"preparedStatement", "params"})
public class Filter {

    private String filterExpressionFromQuery;
    boolean valid = true;
    private Long filterId;
    private String preparedStatement;
    private org.osgi.framework.Filter ldapFilter;
    private Map<String, Object> params;

    public Filter(Request request) {
        this(request, null);
    }

    public Filter(Request request, String defaultFilterExpression) {
        Object filterQuery = request.getAttributes().get(SkysailServerResource.FILTER_PARAM_NAME);
        if (filterQuery != null) {
            this.filterExpressionFromQuery = (String)filterQuery;
        } else if (defaultFilterExpression != null) {
            this.filterExpressionFromQuery = defaultFilterExpression;
        } else {
            //this.filterExpressionFromQuery = "1=1";
        }
        evaluate();
    }

    public Filter(String key, String value) {
        this("(" + key + "=" + value +")");
    }

    public Filter(String filterExpression) {
        this.filterExpressionFromQuery = filterExpression;
        evaluate();
    }

    public void and(String filterExpression) {
        if (filterExpression == null || filterExpression.trim().length() == 0) {
            return;
        }
        if (filterExpressionFromQuery == null) {
            this.filterExpressionFromQuery = filterExpression;
        } else {
            this.filterExpressionFromQuery = "(&"+filterExpressionFromQuery+filterExpression+")";
        }
        evaluate();
    }

    public void add(String key, String value) {
        and("(" + key + "=" + value +")");
        evaluate();
    }

    public void addEdgeOut(String name, String value) {
        and("("+value+" ∈ out['"+name+"'] " +")");
        evaluate();
    }

    private void evaluate() {
        if (filterExpressionFromQuery == null) {
            return;
        }
        try {
            filterId = Long.parseLong(filterExpressionFromQuery);
            return;
        } catch (Exception e) {
            // that's ok
        }
        try {
            Parser parser = new Parser(filterExpressionFromQuery);
            Object accept = parser.parse().accept(new SqlFilterVisitor());
            preparedStatement = ((PreparedStatement) accept).getSql();
            params = ((PreparedStatement)accept).getParams();
            return;
        } catch (InvalidSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        valid = false;
    }

    private class SqlFilterVisitor implements FilterVisitor {

        @Override
        public PreparedStatement visit(ExprNode arg0) {
            if (arg0 instanceof EqualityNode) {
                PreparedStatement ps = new PreparedStatement();
                EqualityNode node = (EqualityNode) arg0;
                ps.append(node.getAttribute()).append("=:").append(node.getAttribute());
                ps.put(node.getAttribute(), node.getValue());
                return ps;
            } else if (arg0 instanceof AndNode) {
                return new PreparedStatement("AND",((AndNode) arg0).getChildList().stream().map(n -> {
                    return visit(n);
                }).collect(Collectors.toList()));
            } else if (arg0 instanceof OrNode) {
                return new PreparedStatement("OR",((OrNode) arg0).getChildList().stream().map(n -> {
                    return visit(n);
                }).collect(Collectors.toList()));
            } else if (arg0 instanceof NotNode) {
                return new PreparedStatement("NOT", Arrays.asList(visit(((NotNode) arg0).getChild())));
            } else if (arg0 instanceof IsInNode) {
                PreparedStatement ps = new PreparedStatement();
                IsInNode node = (IsInNode) arg0;
                ps.append(node.getAttribute()).append(" IN ").append(node.getValue().replace("[", "(").replace("]",")"));
                //ps.put(node.getAttribute(), node.getValue());
                return ps;
            }
            return new PreparedStatement();
        }

    }

}
