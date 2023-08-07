/* basic role insert */
INSERT INTO role
VALUES (1, true, 'Public User'),
       (2, true, 'Administrative User');

/*
 Insert Role Module
 */
/*INSERT INTO role_module(id, is_active, module_name)
VALUES (1, true, 'A-Dashboard'),
       (2, true, 'License'),
       (3, true, 'Blue Book'),
       (4, true, 'User Request'),
       (5, true, 'Number Plate Scan'),
       (6, true, 'OwnerShip Request'),
       (7, true, 'U-Dashboard'),
       (8, true, 'Sell Vehicle'),
       (9, true, 'Buy Request'),
       (10, true, 'Add New Vehicle'),
       (11, true, 'User List');*/

INSERT INTO role_module(id, is_active, module_name)
VALUES (1, true, 'A-Dashboard'),
       (2, true, 'License'),
       (3, true, 'Blue Book'),
       (4, true, 'User Request'),
       (5, true, 'Number Plate Scan'),
       (6, true, 'Tax Clearance Request'),
       (7, true, 'U-Dashboard'),
       (8, true, 'Process Tax'),
       (9, true, 'Add New Vehicle'),
       (10, true, 'User List');

/*
 insert role mapping public user
 */
    INSERT
INTO role_module_mapping(id, is_active, role_id, role_module_id)
VALUES (1, true, 1, 7), (2, true, 1, 8);


INSERT INTO role_module_mapping(id, is_active, role_id, role_module_id)
VALUES (6, true, 2, 1),
       (7, true, 2, 2),
       (8, true, 2, 3),
       (9, true, 2, 4),
       (10, true, 2, 5),
       (11, true, 2, 6),
       (12, true, 2, 10);
/*
 basic module insert
 */

--  insert into administrative_user

-- INSERT INTO public.users (id, address, citizenship_back_url, citizenship_font_url, citizenship_no, email, is_enable, is_new_kyc_request, mobile_number, name, password, profile_image_url, role_id)
-- VALUES (1, null, null, null, null, 'nepalwheels@gmail.com', true, null, '01-4004100', 'Nepal Wheels Pvt. Ltd', '$2a$10$NQiQnDT/6rv6.5rhciH4o.03KV/ZwN1X0cyov5YKIYVZVSJY4s79G', null, 3);


INSERT INTO public.users (id, address, citizenship_back_url, citizenship_font_url, citizenship_no, email, is_enable,
                          is_new_kyc_request, mobile_number, name, password, profile_image_url, role_id)
VALUES (1, null, null, null, null, 'dotm@gmail.com', true, null, '09234322034', 'Department of Transportation',
        '$2a$10$nW3NeXp7oxjxasMiym4zJOaJbZOeUIBNYRXOIOp2EKl9sMTaSBc9e', null, 2);
