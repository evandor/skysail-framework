package de.twenty11.skysail.server.beans;

import org.apache.commons.beanutils.DynaBean;

/**
 * DynamicBeans are based on beanUtils DynaBean and don't need a concrete class
 * to define an entity.
 *
 */
public interface DynamicBean {

    DynaBean getInstance();

}
