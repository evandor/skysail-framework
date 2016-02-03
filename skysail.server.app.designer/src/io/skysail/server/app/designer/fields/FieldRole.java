package io.skysail.server.app.designer.fields;

public enum FieldRole {

    NONE,        // DEFAULT
    MODIFIED_AT, // a date field updated in Put*Resource#update
    GUID         // a globally unique identifier
}
