package io.skysail.app.propman;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.io.Serializable;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties("handler")
@Getter
@Setter
public class Request  implements Serializable, Identifiable {

    @Id
    private String id;
    
    @Field
    private String requestname;
    
}
