package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.text.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Getter
public class Message implements Identifiable {

    @Field
    private String msgKey;

    private String msg;

    private String store;

    private Set<String> availableStores;

    @JsonIgnore
    private Splitter splitter = Splitter.on(".").trimResults();

    @JsonIgnore
    private TranslationRenderService preferredRenderer;

    public Message(String msgKey) {
        this.msgKey = msgKey;
    }

    public Message(String msgKey, String msg) {
        this.msgKey = msgKey;
        this.msg = msg.replace("'{'", "{").replace("'}'", "}");

    }

    public Message(String key, Translation translation, TranslationRenderService preferredRenderer) {
        this(key, translation.getValue());
        this.preferredRenderer = preferredRenderer;
        store = translation.getStore().getClass().getName();
        this.availableStores = translation.getStores();
        if (preferredRenderer != null) {
            this.msg = preferredRenderer.adjustText(this.msg);
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStoreChooser() {
        return availableStores.stream().map(s -> {
            if (s.equals(store)) {
                return "<u>" + simpleName(store) + "</u>";
            }
            return "<a href='?store="+s+"'>" + simpleName(s) + "</a>";
        }).collect(Collectors.joining(" - "));

    }

    private String simpleName(String className) {
        return Iterables.getLast(splitter.split(className));
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
