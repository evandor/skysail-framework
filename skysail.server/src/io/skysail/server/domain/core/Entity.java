package io.skysail.server.domain.core;

import io.skysail.api.domain.Identifiable;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.resource.ServerResource;

@ToString
@Slf4j
public class Entity implements Identifiable {

    private Class<? extends Identifiable> identifiableClass;

    public Entity(String id) {
        this.id = id;
    }

    public Entity(Class<? extends Identifiable> identifiableClass) {
        this.id = identifiableClass.getSimpleName();
        this.identifiableClass = identifiableClass;
    }

    @Getter
    @Setter
    private String id;

    private List<Field> fields = new ArrayList<>();

    private PostResource<Entity> postResource = new PostResource<>();
    private PutResource<Entity> putResource;
    private ListResource<Entity> listResource;
    private EntityResource<Entity> entityResource;

    public Entity add(Field field) {
        this.fields.add(field);
        return this;
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public Class<? extends ServerResource> getPostResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + ".Post" + id + "Resource");
    }

    public Class<? extends ServerResource> getPutResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + ".Put" + id + "Resource");
    }

    public Class<? extends ServerResource> getListResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + "." + id + "sResource");
    }

    public Class<? extends ServerResource> getEntityResourceClass() {
        return getClass(identifiableClass.getPackage().getName() + "." + id + "Resource");
    }

    @SuppressWarnings("unchecked")
    private Class<? extends ServerResource> getClass(String classname) {
        try {
            log.info("searching for class '{}'", classname);
            return (Class<? extends ServerResource>) Class.forName(classname,false,identifiableClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }
}
