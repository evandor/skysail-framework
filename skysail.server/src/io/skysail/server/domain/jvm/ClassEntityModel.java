package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.restlet.resource.ServerResource;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.*;
import io.skysail.server.forms.Tab;
import io.skysail.server.utils.*;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(callSuper = true)
public class ClassEntityModel extends EntityModel {

    private static final String MORE_TAB_NAME = "more...";

    protected Class<? extends Identifiable> identifiableClass;

    private volatile Set<Tab> tabs;
    
    public ClassEntityModel(Class<? extends Identifiable> identifiableClass) {
        super(identifiableClass.getName());
        this.identifiableClass = identifiableClass;
        deriveFields(identifiableClass);
        deriveRelations(identifiableClass);
    }

    public Class<? extends ServerResource> getPostResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(packageOf(identifiableClass.getName()) + ".Post" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + ".Post" + identifiableClass.getSimpleName() + "Resource");
    }

    public Class<? extends ServerResource> getPutResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(packageOf(identifiableClass.getName()) + ".Put" + identifiableClass.getSimpleName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + ".Put" + identifiableClass.getSimpleName() + "Resource");
    }

    public Class<? extends ServerResource> getListResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return getClass(identifiableClass.getName() + "sResource");
        }
        return getClass(identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "sResource");
    }

    public Class<? extends ServerResource> getEntityResourceClass() {
        if (identifiableClass.getPackage() == null) {
            return  getClass(identifiableClass.getName() + "Resource");
        }
        return getClass(identifiableClass.getPackage().getName() + "." + identifiableClass.getSimpleName() + "Resource");
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ServerResource> getClass(String classname) {
        try {
            log.debug("searching for class '{}'", classname);
            return (Class<? extends ServerResource>) Class.forName(classname,false,identifiableClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    private void deriveFields(Class<? extends Identifiable> cls) {
        setFields(ReflectionUtils.getInheritedFields(cls).stream()
            .filter(f -> filterFormFields(f))
            .map(f -> new ClassFieldModel(f))
            .collect(MyCollectors.toLinkedMap(ClassFieldModel::getId, Function.identity())));
    }

    private boolean filterFormFields(Field f) {
        return f.getAnnotation(io.skysail.domain.html.Field.class) != null;
    }
    
    private String packageOf(String fullQualifiedName) {
        String[] split = fullQualifiedName.split("\\.");
        return Arrays.stream(Arrays.copyOf(split, split.length-1)).collect(Collectors.joining("."));
    }
    
    private void deriveRelations(Class<? extends Identifiable> cls) {
        setRelations(ReflectionUtils.getInheritedFields(cls).stream()
            .filter(f -> filterRelationFields(f))
            .map(f -> f.getName())
            .map(r -> new EntityRelation(r, null, EntityRelationType.ONE_TO_MANY))
            .collect(Collectors.toList()));
    }
    
    private boolean filterRelationFields(Field f) {
        return f.getAnnotation(io.skysail.domain.html.Relation.class) != null;
    }

    public synchronized Set<Tab> getTabs() {
        if (tabs != null) {
            return tabs;
        }
        Set<String> tabNamesSet = getFieldValues().stream()
            .map(ClassFieldModel.class::cast)
            .map(f -> f.getPostTabName())
            .filter(name -> name != null)
            //.map(name -> name == "" ? MORE_TAB_NAME : name)
            .collect(Collectors.toSet());

//        List<String> tabNamesList = getFieldValues().stream()
//                .map(ClassFieldModel.class::cast)
//                .map(f -> f.getPostTabName())
//                .filter(name -> name != null)
//                .map(name -> name == "" ? MORE_TAB_NAME : name)
//                .collect(Collectors.toList());
//        if (tabNamesList.isEmpty() || (tabNamesSet.size() == 1 && tabNamesSet.iterator().next().equals(MORE_TAB_NAME))) {
//            return Collections.emptyList();
//        }
        if (tabNamesSet.isEmpty() || tabNamesSet.size() == 1) {
            return Collections.emptySet();
        }
       
        
//        List<io.skysail.server.forms.Tab2.Tab> tabsList = tabNamesSet.stream().map(tabIdentifier -> {
//            Optional<io.skysail.server.forms.Tab2.Tab> theTab = Tab2.getTab(tabIdentifier);
//            if (theTab.isPresent()) {
//                return theTab.get();
//            }
//            return null;
//        })
//        .filter(t -> t != null)        
//        .collect(Collectors.toList());
        
//        if (tabNamesSet.size() > tabsList.size()) {
//            //tabsList.add( io.skysail.server.forms.Tab2.Tab("more...", "more..."));
//        }
//        
        tabs = new HashSet<>();
        int i = 0;
        for (String aTab : tabNamesSet) {
            //if (tabNamesSet.contains(tabNameFromList)) {
                tabs.add(new Tab(aTab,aTab, i++));
               // tabNamesSet.remove(tabNameFromList);
            //}
        }
        
        return tabs;
    }


}
