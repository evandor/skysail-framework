package de.twenty11.skysail.api.domain;

/**
 * used as common interface to describe the sorting of items returned in a List.
 * 
 * The itemNr n is set to the nth element of the list.
 * 
 */
public interface Sorted {

    long getItemNr();

}
