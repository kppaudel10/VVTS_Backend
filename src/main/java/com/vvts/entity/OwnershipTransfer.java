package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @auther kul.paudel
 * @created at 2023-04-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ownership_transfer")
public class OwnershipTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ownership_transfer_gen")
    @SequenceGenerator(name = "ownership_transfer_gen", sequenceName = "ownership_transfer_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "request_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date transferRequestDate;

    @Column(name = "approve_date")
    @Temporal(TemporalType.DATE)
    private Date transferApproveDate;

    @Column(name = "is_approve_by_owner")
    private Boolean isApproveByOwner = false;

    @Column(name = "is_approve_by_admin")
    private Boolean isApproveByAdmin = false;

    @Column(name = "vehicle_identification_no",nullable = false)
    private String vehicleIdentificationNo;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Users buyer;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Users seller;

}
