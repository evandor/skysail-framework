package io.skysail.server.queryfilter;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.directory.api.ldap.model.filter.*;

@Getter
@Slf4j
public class Filter {

    private String filterExpressionFromQuery;

    boolean valid = true;

    private Long filterId;

    private ExprNode ldapFilter;

    private String sqlExpression;

    public Filter(@NonNull String filterExpression) {
        this.filterExpressionFromQuery = filterExpression;
        try {
            filterId = Long.parseLong(filterExpression);
            return;
        } catch (Exception e) {
        }
        try {
            ldapFilter = FilterParser.parse(filterExpression);
            sqlExpression = ((StringBuilder) ldapFilter.accept(new SqlFilterVisitor())).toString();
            return;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        valid = false;
    }

    private class SqlFilterVisitor implements FilterVisitor {

        @Override
        public Object visit(ExprNode arg0) {
            StringBuilder sb = new StringBuilder();
            if (arg0 instanceof EqualityNode) {
                EqualityNode<?> node = (EqualityNode<?>) arg0;
                sb.append(node.getAttribute()).append("=").append(node.getEscapedValue().toString());
                return sb;
            } else if (arg0 instanceof NotNode) {
                NotNode node = (NotNode) arg0;
                return new StringBuilder("NOT ").append(visit(node.getFirstChild()));
            } else if (arg0 instanceof AndNode) {
                return new StringBuilder(((AndNode) arg0).getChildren().stream().map(n -> {
                    return visit(n).toString();
                }).collect(Collectors.joining(" AND ")));
            } else if (arg0 instanceof OrNode) {
                return new StringBuilder(((OrNode) arg0).getChildren().stream().map(n -> {
                    return visit(n).toString();
                }).collect(Collectors.joining(" OR ")));
            } else if (arg0 instanceof SubstringNode) {
                SubstringNode node = (SubstringNode) arg0;
                return sb.append(node.getAttribute()).append(" LIKE ").append("'%" + node.getAny().get(0) + "%'");
            }
            return sb;
        }

        @Override
        public boolean isPrefix() {
            return false;
        }

        @Override
        public List<ExprNode> getOrder(BranchNode arg0, List<ExprNode> arg1) {
            return null;
        }

        @Override
        public boolean canVisit(ExprNode arg0) {
            if (arg0 instanceof EqualityNode || arg0 instanceof AndNode || arg0 instanceof OrNode
                    || arg0 instanceof SubstringNode || arg0 instanceof NotNode) {
                return true;
            }
            return false;
        }
    };
}
