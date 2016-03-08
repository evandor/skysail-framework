package io.skysail.server.designer.demo.accounts.transaction;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import io.skysail.server.designer.demo.accounts.account.*;
import io.skysail.server.designer.demo.accounts.account.resources.*;
import io.skysail.server.designer.demo.accounts.transaction.*;
import io.skysail.server.designer.demo.accounts.transaction.resources.*;


import org.apache.commons.lang3.StringUtils;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Transaction implements Identifiable, Serializable {

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

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date day;

    public void setDay(Date value) {
        this.day = value;
    }

    public Date getDay() {
        return this.day;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String amount;

    public void setAmount(String value) {
        this.amount = value;
    }

    public String getAmount() {
        return this.amount;
    }


    // --- relations ---



}