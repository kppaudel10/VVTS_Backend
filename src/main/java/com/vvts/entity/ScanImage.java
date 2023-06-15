package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @auther kul.paudel
 * @created at 2023-06-15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "scan_image")
public class ScanImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scan_image_gen")
    @SequenceGenerator(name = "scan_image_gen", sequenceName = "scan_image_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "scan_image_url", nullable = false)
    private String scanImageURl;

    @Column(name = "scan_image_name", nullable = false)
    private String scanImageName;

    @Column(name = "scan_image_hash_value", nullable = false)
    private String scanImageHasValue;

    @Column(name = "scan_result")
    private String scanResult;

}
