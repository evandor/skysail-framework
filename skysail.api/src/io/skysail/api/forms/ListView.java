package io.skysail.api.forms;

public enum ListView {
    SHOW, // default, column is shown in List
    HIDE, // column is hidden
    TRUNCATE, // if the column is too long, the text is truncated
    LINK, // a link to the associated entity is created
    PREFIX, // something (defined in TODO will be prepended) to this field in the list view
    SUFFIX // something (defined in TODO will be appended) to this field in the list view
}
