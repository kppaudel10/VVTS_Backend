package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tax_clearance")
public class TaxClearance {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_clearance_gen")
    @SequenceGenerator(name = "tax_clearance_gen", sequenceName = "tax_clearance_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paid_user_id", referencedColumnName = "id")
    private Users paidUser;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private VehicleDetail vehicleDetail;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Column(name = "paid_bill_doc_url")
    private String paidBillDocUrl;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_rejected")
    private Boolean isRejected = false;

    @Column(name = "valid_year")
    private String validYear;

    @Column(name = "process_date")
    private Date processDate;

}
