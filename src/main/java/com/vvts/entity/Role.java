package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-18
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
