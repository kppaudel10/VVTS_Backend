package com.vvts.repo;

import com.vvts.entity.Role;
import com.vvts.projection.InitProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-22
 */
public interface RoleRepo extends JpaRepository<Role, Integer> {

    @Query(value = "select * from role where lower(role_name) = lower(?1)", nativeQuery = true)
    Role getRoleByRoleName(String roleName);

    @Query(value = "select r.role_name as \"roleName\", r.id as \"roleId\", rm.module_name as \"moduleName\", rm.id as \"moduleId\"\n" +
            "from role_module_mapping rmm\n" +
            "         inner join role r on rmm.role_id = r.id and r.is_active = true\n" +
            "         inner join role_module rm on rmm.role_module_id = rm.id and rm.is_active = true\n" +
            "where rmm.role_id = ?1\n" +
            "  and rmm.is_active = true", nativeQuery = true)
    List<InitProjection> getUserModuleAccess(Integer roleId);

}
