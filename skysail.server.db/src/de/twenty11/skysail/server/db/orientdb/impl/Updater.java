package de.twenty11.skysail.server.db.orientdb.impl;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jettison.json.JSONObject;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

@Slf4j
public class Updater extends AbstractDbAPI {

    private OrientGraph db;

    public Updater(OrientGraph db) {
        this.db = db;
    }

    public <T> Object update(JSONObject json) {
        return runInTransaction(db, json);
    }

    protected <T> Object execute(OrientGraph db, Object entity) {
        try {
            System.out.println(entity);
            JSONObject json = (JSONObject) entity;
            OrientVertex vertex = db.getVertex(json.get("_id"));
            vertex.setProperty("name", json.getString("name"));

            // OrientVertex vertexFromJson = (OrientVertex)
            // GraphSONUtility.vertexFromJson((JSONObject) entity,
            // new GraphElementFactory(db), GraphSONMode.NORMAL, null);
            // System.out.println(vertexFromJson);
            vertex.save();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
