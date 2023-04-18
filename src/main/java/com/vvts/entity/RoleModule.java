package com.vvts.entity;
import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@Entity
@Table(name = "role_module")
public class RoleModule {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "module_name",nullable = false)
    private String moduleName;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive = true;

}
