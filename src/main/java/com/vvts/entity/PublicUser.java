package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "public_user",uniqueConstraints = {
        @UniqueConstraint(name = "UniqueEmailAddressPublicUser", columnNames = {"email"}),
        @UniqueConstraint(name = "UniqueMobileNumberPublicUser",columnNames = {"mobile_number"})})
public class PublicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public_user_GEN")
    @SequenceGenerator(name = "public_user_GEN", sequenceName = "public_user_SEQ")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String address;

    @Column(nullable = false)
    private String email;

    @Column(name = "mobile_number",nullable = false)
    private String mobileNumber;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "is_enable")
    private Boolean isEnable;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

}
