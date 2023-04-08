package com.vvts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "public_user")
public class PublicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public_user_GEN")
    @SequenceGenerator(name = "public_user_GEN", sequenceName = "public_user_SEQ")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @Column(name = "mobile_number",nullable = false)
    private String mobileNumber;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "is_enable")
    private Boolean isEnable;


}
