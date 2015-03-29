package de.twenty11.skysail.api.domain;

/**
 * used as common interface for (persistable) entities (with a single
 * identity field called "id").
 * 
 */
public interface Identifiable {

    String getId();

    void setId(String id);

}
