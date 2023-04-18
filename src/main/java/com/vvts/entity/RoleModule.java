package com.vvts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
