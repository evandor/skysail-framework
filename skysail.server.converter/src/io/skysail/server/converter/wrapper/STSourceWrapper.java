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

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ")
//                .append(source.getClass().getName()).append(", isForm: ").append(isForm());
//        if (source instanceof SkysailResponse) {
//            Object entity = ((SkysailResponse<?>) source).getEntity();
//            sb.append("<br>Entity: ").append(entity == null ? "null" : entity.toString());
//        }
//        return sb.toString();
//    }

}
