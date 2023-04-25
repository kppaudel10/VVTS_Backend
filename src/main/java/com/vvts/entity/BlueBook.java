package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class BlueBook {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blue_book_gen")
    @SequenceGenerator(name = "blue_book_gen", sequenceName = "blue_book_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "citizenship_no", nullable = false)
    private String citizenshipNo;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(name = "vehicle_identification_no", nullable = false)
    private String vehicleIdentificationNo;

}
