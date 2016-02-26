package io.skysail.server.app.bpmnmodeler;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Model implements Identifiable, Serializable {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    // --- fields ---

    // --- relations ---



}