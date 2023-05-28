/* basic role insert */
INSERT INTO role
VALUES (1, true, 'Public User'),
       (2, true, 'DOTM Admin'),
       (3, true, 'Vendor Admin');

/*
 Insert Role Module
 */
INSERT INTO role_module(id, is_active, module_name)
VALUES (1, true, 'A-Dashboard'),
       (2, true, 'License'),
       (3, true, 'Blue Book'),
       (4, true, 'User Request'),
       (5, true, 'Number Plate Scan'),
       (6, true, 'OwnerShip Request'),
       (7, true, 'U-Dashboard'),
       (8, true, 'Sell Vehicle'),
       (9, true, 'Buy Request'),;
    (10, true, 'Add Vehicle');

/*
 insert role mapping
 */
INSERT INTO role_module_mapping(id, is_active, role_id, role_module_id)
VALUES (1, true, 2, 1),
       (2, true, 2, 2),
       (3, true, 2, 3),
       (4, true, 2, 4),
       (5, true, 2, 5),
       (6, true, 2, 6),
       (7, true, 2, 7);

INSERT INTO role_module_mapping(id, is_active, role_id, role_module_id)
VALUES (1, true, 1, 1),
       (2, true, 1, 2),
       (3, true, 1, 3),
       (4, true, 1, 4),
       (5, true, 1, 5),
       (6, true, 1, 6),
       (7, true, 1, 7),
       (8, true, 1, 8),
       (9, true, 1, 9),
       (10, true, 1, 10);
/*
 basic module insert
 */

--  insert into administrative_user
--  values (1,'Kathmandu','DOTM Admin','$2a$10$yrVs0HnMxWhAc7IzFNtg5e9dG.LTThV69Apym6hZj6BCVkOFGPOpK','admin-dotm',2);
--  values (2,'Kathmandu','Vendor Admin','$2a$10$yrVs0HnMxWhAc7IzFNtg5e9dG.LTThV69Apym6hZj6BCVkOFGPOpK','admin-vendor',3);
