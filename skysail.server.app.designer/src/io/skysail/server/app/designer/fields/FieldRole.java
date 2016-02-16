package io.skysail.server.app.designer.fields;

public enum FieldRole {

    NONE,        // DEFAULT
    MODIFIED_AT, // a date field updated in Put*Resource#update
    GUID,        // a globally unique identifier (String) set in Post*Resource#addEntity
    OWNER        // a string identifying the entities' owner, set in Post*Resource#addEntity
}
