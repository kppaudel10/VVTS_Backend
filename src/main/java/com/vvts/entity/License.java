package com.vvts.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "license")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "license_GEN")
    @SequenceGenerator(name = "license_GEN", sequenceName = "license_SEQ")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "license_no",nullable = false)
    private String licenseNo;

    @Column(name = "citizenship_no",nullable = false)
    private String citizenshipNo;

    @Column(name = "valid_date",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date validDate;

    @Column(name = "district")
    private String district;
}
