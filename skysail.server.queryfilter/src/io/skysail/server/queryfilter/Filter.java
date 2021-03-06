package io.skysail.server.queryfilter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.osgi.framework.InvalidSyntaxException;
import org.restlet.Request;

import io.skysail.server.queryfilter.nodes.*;
import io.skysail.server.queryfilter.parser.Parser;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString(of = {"preparedStatement", "params"})
public class Filter {

    private String filterExpressionFromQuery;
    boolean valid = true;
    private Long filterId;
    private String preparedStatement = "";
    private org.osgi.framework.Filter ldapFilter;
    private Map<String, Object> params;

    public Filter() {
        this((Request)null, null);
    }

    public Filter(Request request) {
        this(request, null);
    }

    public Filter(Request request, String defaultFilterExpression) {
        if (request == null) {
            return;
        }
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

    public Filter add(String key, String value) {
        and("(" + key + "=" + value +")");
        evaluate();
        return this;
    }

    public void addEdgeOut(String name, String value) {
        and("("+value+" ∈ out['"+name+"'] " +")");
        evaluate();
    }

    public void addEdgeIn(String name, String value) {
        and("("+value+" ∈ in['"+name+"'] " +")");
        evaluate();
    }

    private void evaluate() {
        if (filterExpressionFromQuery == null) {
            return;
        }
        try {
            filterId = Long.parseLong(filterExpressionFromQuery); // TODO remove filter id logic
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
            PreparedStatement ps = new PreparedStatement();
            if (arg0 instanceof EqualityNode) {

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
                IsInNode node = (IsInNode) arg0;
                ps.append(node.getAttribute()).append(" IN ").append(node.getValue().replace("[", "(").replace("]",")"));
                return ps;
            } else if (arg0 instanceof LessNode) {
                LessNode node = (LessNode) arg0;
                if (node.getValue().contains("(")) {
                    ps.append(node.getAttribute()).append(" < ").append(node.getValue());
                } else {
                    ps.append(node.getAttribute()).append("<:").append(node.getAttribute());
                    ps.put(node.getAttribute(), node.getValue());
                }
                return ps;
            } else if (arg0 instanceof GreaterNode) {
                GreaterNode node = (GreaterNode) arg0;
                if (node.getValue().contains("(")) {
                    ps.append(node.getAttribute()).append(" > ").append(node.getValue());
                } else {
                    ps.append(node.getAttribute()).append(">:").append(node.getAttribute());
                    ps.put(node.getAttribute(), node.getValue());
                }
                return ps;
            } else if (arg0 instanceof PresentNode) {
                PresentNode node = (PresentNode) arg0;
                ps.append(node.getAttribute()).append(" is ").append(" NOT NULL");
                return ps;
            } else if (arg0 instanceof SubstringNode) {
                SubstringNode node = (SubstringNode) arg0;
                ps.append(node.getAttribute()).append(" containstext '").append(node.getValue()).append("'");
                return ps;
            } else {
                throw new IllegalStateException("cannot visit node of type " + arg0.getClass());
            }
        }

    }

}
