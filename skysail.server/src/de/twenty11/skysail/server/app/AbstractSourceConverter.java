package de.twenty11.skysail.server.app;


public abstract class AbstractSourceConverter {

//    final protected ObjectMapper mapper = new ObjectMapper();
//
//    private Object source;
//    private Variant target;
//
//    public void init(Object source, Variant target) {
//        this.source = source;
//        this.target = target;
//    }
//
//    protected Comparator<String> getComparator(SkysailServerResource<?> resource) {
//        return null;
//    }
//
//    protected Optional<Method> findHandlerMethod(Method[] methods) {
//        Optional<Method> getHandlerMethod = Arrays.stream(methods).filter(m -> m.getName().contains("getHandler"))
//                .findFirst();
//        return getHandlerMethod;
//    }
//    
//    protected SortedMap<String, Object> mapSource2(Object source, Map<String, Object> map, SkysailServerResource<?> resource) {
//        SortedMap<String, Object> objectMap = new TreeMap<>(getComparator(resource));
//        Map<String, Object> collect = map.entrySet().stream().filter(entry -> {
//            return !entry.getKey().startsWith("@") || entry.getKey().equals("@rid");
//        }).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue() == null ? "&lt;null&gt;" : p.getValue()));
//        objectMap.putAll(collect);
//        return objectMap;
//    }
//
//    public Object getSource() {
//        return source;
//    }
//
//    public Variant getTarget() {
//        return target;
//    }

}
