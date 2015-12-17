package io.skysail.server.app.designer.exceptions;

public class MappingException extends RuntimeException {

    private static final long serialVersionUID = -987009859160547869L;

    public MappingException(String message, Exception e) {
        super(message, e);
    }


}
