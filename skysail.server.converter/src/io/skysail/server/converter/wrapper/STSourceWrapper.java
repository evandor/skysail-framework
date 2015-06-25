package io.skysail.server.converter.wrapper;


public class STSourceWrapper {

    private Object source;

    public STSourceWrapper(Object source) {
        this.source = source;
    }

    public String getEntityType() {
        return this.source.getClass().getName();
    }

    
    

    public Object getEntity() {
        return source;
    }


}
