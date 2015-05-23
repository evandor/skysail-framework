package io.skysail.server.queryfilter;


public interface FilterVisitor {

    PreparedStatement visit( ExprNode node );

}
