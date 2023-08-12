package com.vvts.entity;

import com.vvts.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Entity
@Table(name = "vehicle_detail")
public class VehicleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_detail_gen")
    @SequenceGenerator(name = "vehicle_detail_gen", sequenceName = "vehicle_detail_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "identification_no", nullable = false)
    private String vehicleIdentificationNo;

    @Column(name = "manufacture_year", nullable = false)
    private String manufactureYear;

    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    private Integer cc;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id")
    private Users vendor;

}
