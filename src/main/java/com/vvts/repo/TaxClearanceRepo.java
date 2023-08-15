package com.vvts.repo;

import com.vvts.entity.TaxClearance;
import com.vvts.projection.TaxClearanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
public interface TaxClearanceRepo extends JpaRepository<TaxClearance, Integer> {

    @Query(value = "select distinct tc.id, to_char(tc.process_date, 'yyyy-MM-dd')                as \"processDate\",\n" +
            "                u.citizenship_no                                      as \"citizenshipNo\",\n" +
            "                vd.vehicle_type                                       as \"vehicleType\",\n" +
            "                vd.identification_no                                  as \"identificationNo\",\n" +
            "                bb.number_plate                                       as \"numberPlate\",\n" +
            "                tc.paid_amount                                        as \"paidAmount\",\n" +
            "                tc.valid_year                                         as \"validYear\",\n" +
            "                tc.is_verified                                        as \"isVerified\",\n" +
            "                SUBSTRING(tc.paid_bill_doc_url FROM '.*/([^/]+)$') AS \"taxPaidBillImageName\"\n" +
            "from tax_clearance tc\n" +
            "         inner join users u on tc.paid_user_id = u.id\n" +
            "         inner join vehicle_detail vd on tc.vehicle_id = vd.id\n" +
            "         left join blue_book bb on bb.vehicle_identification_no = vd.identification_no\n" +
            "where tc.paid_user_id = ?1", nativeQuery = true)
    List<TaxClearanceProjection> getTaxClearanceByLoginUserId(Integer userId);

    @Modifying
    @Query(value = "update tax_clearance set is_verified = true where id = ?1", nativeQuery = true)
    void acceptTaxClearanceRequest(Integer taxClearanceId);

    @Modifying
    @Query(value = "update tax_clearance set is_verified = true where id = ?1", nativeQuery = true)
    void rejectTaxClearanceRequest(Integer taxClearanceId);

    @Query(value = "select distinct tc.id, to_char(tc.process_date, 'yyyy-MM-dd')                as \"processDate\",\n" +
            "                u.citizenship_no                                      as \"citizenshipNo\",\n" +
            "                vd.vehicle_type                                       as \"vehicleType\",\n" +
            "                vd.identification_no                                  as \"identificationNo\",\n" +
            "                bb.number_plate                                       as \"numberPlate\",\n" +
            "                tc.paid_amount                                        as \"paidAmount\",\n" +
            "                tc.valid_year                                         as \"validYear\",\n" +
            "                tc.is_verified                                        as \"isVerified\",\n" +
            "                SUBSTRING(tc.paid_bill_doc_url FROM '.*/([^/]+)$') AS \"taxPaidBillImageName\"\n" +
            "from tax_clearance tc\n" +
            "         inner join users u on tc.paid_user_id = u.id\n" +
            "         inner join vehicle_detail vd on tc.vehicle_id = vd.id\n" +
            "         left join blue_book bb on bb.vehicle_identification_no = vd.identification_no\n" +
            "where tc.is_verified = false and tc.is_rejected = false", nativeQuery = true)
    List<TaxClearanceProjection> getTaxClearanceRequestForAdmin();
}
