package com.vvts.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @auther kul.paudel
 * @created at 2023-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "pin_code")
public class PinCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pin_code_gen")
    @SequenceGenerator(name = "pin_code_gen", sequenceName = "pin_code_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Column(name = "expired_date", nullable = false)
    private Date expiredDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users users;

}
