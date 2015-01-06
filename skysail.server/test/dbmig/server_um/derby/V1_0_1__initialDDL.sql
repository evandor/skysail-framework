CREATE TABLE um_users(
    ID VARCHAR(64) NOT NULL ,
    USERNAME VARCHAR(255) ,
    PASSWORD VARCHAR(255) ,
    SALT VARCHAR(255) ,
    CONSTRAINT um_users_pk PRIMARY KEY(ID)
); 

CREATE TABLE um_roles(
    ID VARCHAR(64) NOT NULL ,
    NAME VARCHAR(255) ,
    CONSTRAINT um_roles_pk PRIMARY KEY(ID)
); 

CREATE TABLE um_users_um_roles(
    SkysailUser_ID VARCHAR(64) NOT NULL ,
    roles_ID VARCHAR(64) NOT NULL ,
    CONSTRAINT um_users_um_roles_pk PRIMARY KEY(
        SkysailUser_ID ,
        roles_ID
    )
); 

ALTER TABLE um_users_um_roles 
ADD CONSTRAINT FK_um_users_um_roles_roles_ID FOREIGN KEY(roles_ID) REFERENCES um_roles(ID); 

ALTER TABLE um_users_um_roles 
ADD CONSTRAINT FK_um_users_um_roles_SkysailUser_ID FOREIGN KEY(SkysailUser_ID) REFERENCES um_users(ID); 

CREATE TABLE um_groups(
    ID VARCHAR(64) NOT NULL ,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255),
    PERMISSIONS VARCHAR(4) DEFAULT '0700' NOT NULL,
    OWNER VARCHAR(64) NOT NULL ,
    CONSTRAINT um_groups_pk PRIMARY KEY(ID)
); 

ALTER TABLE um_groups 
ADD CONSTRAINT FK_um_groups_owner_ID FOREIGN KEY(OWNER) REFERENCES um_users(ID); 


CREATE TABLE um_users_um_groups(
    SkysailUser_ID VARCHAR(64) NOT NULL ,
    GROUPS_ID VARCHAR(64) NOT NULL ,
    CONSTRAINT um_users_um_groups_pk PRIMARY KEY(
        SkysailUser_ID ,
        GROUPS_ID
    )
); 

ALTER TABLE um_users_um_groups 
ADD CONSTRAINT FK_um_users_um_groups_roles_ID FOREIGN KEY(GROUPS_ID) REFERENCES um_groups(ID); 

ALTER TABLE um_users_um_groups 
ADD CONSTRAINT FK_um_users_um_groups_SkysailUser_ID FOREIGN KEY(SkysailUser_ID) REFERENCES um_users(ID); 


