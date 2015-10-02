package io.skysail.server.db.versions.impl;

import io.skysail.api.domain.Identifiable;

import javax.persistence.Id;

import lombok.*;

import org.osgi.framework.Bundle;

@Data
@NoArgsConstructor
public class ComponentDbVersion implements Identifiable {

    @Id
    private String id;

    private String entity;

    private Integer version;

    public ComponentDbVersion(Bundle bundle) {
        this.entity = bundle.getSymbolicName();
        this.version = 0;
    }

}
