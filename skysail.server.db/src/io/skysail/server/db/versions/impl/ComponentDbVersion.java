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

    private String title;

    private Integer version = 0;

    private Status status = Status.NEW;

    public ComponentDbVersion(Bundle bundle) {
        this.entity = bundle.getSymbolicName();
    }

    public ComponentDbVersion(Bundle bundle, String title, Integer version) {
        this.entity = bundle.getSymbolicName();
        this.title = title;
        this.version = version;
    }

}
