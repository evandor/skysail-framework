package de.twenty11.skysail.server.db.orientdb.impl;

import java.util.Date;

import lombok.Getter;

@Getter
public class DbVersion {
    
    private Long version;
    private String remark;
    private Date created;

    public DbVersion() {
    }
    
    public DbVersion(long version, String remark) {
        this.version = version;
        this.remark = remark;
        this.created = new Date();
    }

}
