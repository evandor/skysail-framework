package de.twenty11.skysail.api.domain;

/**
 * used as common interface for (persistable) entities (which have a single identity field)
 * 
 */
public interface Identifiable {

    String getId();

    void setId(String id);

}
