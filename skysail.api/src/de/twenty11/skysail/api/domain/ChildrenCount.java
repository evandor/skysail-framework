package de.twenty11.skysail.api.domain;

/**
 * used as common interface to provide the number of children of this element
 * 
 */
public interface ChildrenCount {

    /**
     * @return the number of children of this element (... the current user actually is allowed to access).
     */
    long getNrOfChildren();

}
