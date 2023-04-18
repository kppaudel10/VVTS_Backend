package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "administrative_user")
public class AdministrativeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "administrative_user_GEN")
    @SequenceGenerator(name = "administrative_user_GEN", sequenceName = "administrative_user_SEQ")
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    private String address;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}