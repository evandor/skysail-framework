package io.skysail.domain;

/**
 * used as common interface for (potentially persistable) entities (with a single
 * identity field called "id").
 * 
 */
public interface Identifiable {

    String getId();

    void setId(String id);

}
