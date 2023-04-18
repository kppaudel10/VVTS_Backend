package com.vvts.entity;

import jakarta.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@Entity
@Table(name = "role_module_mapping")
public class RoleModuleMapping {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "role_module_id",referencedColumnName = "id")
    private RoleModule roleModule;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
