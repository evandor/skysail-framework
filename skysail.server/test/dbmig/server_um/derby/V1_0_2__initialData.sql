INSERT INTO um_users (id, username, password, salt) VALUES ('1', 'system',   'skysail','');
INSERT INTO um_users (id, username, password, salt) VALUES ('2', 'Izzy',  '05646af87a188c337cf20217fbaf3409db8f658ee3d7b153cde55cb2708ab8067be819043204218e27e1b6a14c0a1a057cb18d433b2a5dbd0396fc1d9fd30e13','3361396639323137663232303431346662333236663831333636366430303164');
INSERT INTO um_users (id, username, password, salt) VALUES ('3', 'Linus', '00b348a7b0f5d1e5e7468bb440def3b87edb764ca0668ab2b266d9e97982475025923a116c2ab469a5cf60983452e8c9f9484a7179c6aefa214af6802a3caaad','3130643564386239316530306538366562653066386434323131633133336634');

INSERT INTO um_roles (id, name) VALUES ('1', 'system');
INSERT INTO um_roles (id, name) VALUES ('2', 'admin');

INSERT INTO um_users_um_roles (SkysailUser_ID, roles_ID) VALUES ('1', '1');
INSERT INTO um_users_um_roles (SkysailUser_ID, roles_ID) VALUES ('2', '2');

INSERT INTO um_groups (id, name, description, permissions, owner) VALUES ('1', 'private', 'system group: entity is private', '0744', '1');
INSERT INTO um_groups (id, name, description, permissions, owner) VALUES ('2', 'public', 'system group: entity is public for all users of the system', '0744', '1');
INSERT INTO um_groups (id, name, description, permissions, owner) VALUES ('3', 'world public', 'system group: entity is public even for anonymous users', '0744', '1');






