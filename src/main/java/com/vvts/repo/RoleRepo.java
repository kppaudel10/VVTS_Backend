package com.vvts.repo;

import com.vvts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-04-22
 */
public interface RoleRepo extends JpaRepository<Role, Integer> {

    @Query(value = "select * from role where lower(role_name) = lower(?1)", nativeQuery = true)
    Role getRoleByRoleName(String roleName);

}
