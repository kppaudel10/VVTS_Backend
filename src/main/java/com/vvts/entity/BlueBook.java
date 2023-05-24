package com.vvts.entity;

import com.vvts.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private VehicleType vehicleType;

    @Column(name = "vehicle_identification_no", nullable = false)
    private String vehicleIdentificationNo;

    @Column(name = "effective_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;

    @Column(name = "number_plate", nullable = false)
    private String numberPlate;

}
