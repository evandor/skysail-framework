package io.skysail.server.app.dbviewer.dbclasses;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import lombok.*;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Getter
@Setter
@ToString
public class DbClass implements Identifiable, Comparable<DbClass> {

    public DbClass(OrientVertex v) {
        ODocument record = v.getRecord();
        this.name = (String)record.field("name");
        this.id = this.name;
    }

    public DbClass(String name) {
        this.name = name;
        this.id = name;
    }

    private String id;

    @Field
    private String name;

    @Override
    public int compareTo(DbClass o) {
        return this.name.compareTo(o.name);
    }
}
