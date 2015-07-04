package io.skysail.app.propman;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.forms.*;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("handler")
@Getter
@Setter
public class Campaign  implements Serializable, Identifiable {

    @Id
    private String id;
    
    @Field
    private String name;
    
    @Reference(cls = Request.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    private List<Request> requests = new ArrayList<>();

    public void addRequest(Request entity) {
        requests.add(entity);
    }
    
    


    
}
