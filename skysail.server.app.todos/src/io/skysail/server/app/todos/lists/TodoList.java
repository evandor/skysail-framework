package io.skysail.server.app.todos.lists;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Getter
@Setter
@ToString(of = { "name" })
@JsonPropertyOrder({ "title", "desc" })
public class TodoList implements Serializable {

        /**
     * 
     */
    private static final long serialVersionUID = -3188923584006747102L;

        @Id
        private String id;

        @Field(listView = { ListView.TRUNCATE, ListView.LINK })
        @NotNull
        @Size(min = 2)
        private String name;

        @Field(type = InputType.TEXTAREA, listView = { ListView.TRUNCATE })
        private String desc;

        @Field(type = InputType.READONLY)
        private Date created;

        @Field(type = InputType.READONLY)
        private Date modified;

        @Field(type = InputType.READONLY, listView = { ListView.HIDE })
        private String owner;

    }