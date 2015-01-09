package de.twenty11.skysail.api.responses;

import java.util.List;

import aQute.bnd.annotation.ConsumerType;

@ConsumerType
public interface Linking {

    /**
     * http linkheader.
     * 
     * @return list of links
     */
    List<Linkheader> getLinkheader();
    

}
