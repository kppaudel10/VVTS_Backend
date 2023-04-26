package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "access_token", uniqueConstraints = {
        @UniqueConstraint(name = "userNameUniqueConstraint", columnNames = "user_name")})
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_token_gen")
    @SequenceGenerator(name = "access_token_gen", sequenceName = "access_token_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "access_token", length = 500)
    private String accessToken;

}
