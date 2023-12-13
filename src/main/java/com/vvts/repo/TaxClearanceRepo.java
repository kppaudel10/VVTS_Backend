package com.vvts.repo;

import com.vvts.entity.TaxClearance;
import com.vvts.projection.TaxClearanceProjection;
import com.vvts.projection.UserCommonDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-08-08
 */
public interface TaxClearanceRepo extends JpaRepository<TaxClearance, Integer> {

    @Query(value = "select distinct tc.id,\n" +
            "                to_char(tc.process_date, 'yyyy-MM-dd')             as \"processDate\",\n" +
            "                u.citizenship_no                                   as \"citizenshipNo\",\n" +
            "                vd.vehicle_type                                    as \"vehicleType\",\n" +
            "                vd.identification_no                               as \"identificationNo\",\n" +
            "                tc.paid_amount                                     as \"paidAmount\",\n" +
            "                tc.valid_year                                      as \"validYear\",\n" +
            "                tc.is_verified                                     as \"isVerified\",\n" +
            "                tc.is_rejected                                     as \"isRejected\",\n" +
            "                SUBSTRING(tc.paid_bill_doc_url FROM '.*/([^/]+)$') AS \"taxPaidBillImageName\",\n" +
            "                case\n" +
            "                    when tc.is_verified is true then 'Verified'\n" +
            "                    when tc.is_rejected is true then 'Rejected'\n" +
            "                    else 'Pending' end                             as status\n" +
            "from tax_clearance tc\n" +
            "         inner join users u on tc.paid_user_id = u.id\n" +
            "         inner join vehicle_detail vd on tc.vehicle_id = vd.id\n" +
            "where tc.paid_user_id = ?1", nativeQuery = true)
    List<TaxClearanceProjection> getTaxClearanceByLoginUserId(Integer userId);

    @Modifying
    @Query(value = "update tax_clearance set is_verified = true , verified_date = ?2 , valid_year = ?3 where id = ?1", nativeQuery = true)
    void acceptTaxClearanceRequest(Integer taxClearanceId, Date verifiedDate, String validYear);

    @Modifying
    @Query(value = "update tax_clearance set is_verified = true where id = ?1", nativeQuery = true)
    void rejectTaxClearanceRequest(Integer taxClearanceId);

    @Query(value = "select distinct tc.id,\n" +
            "                to_char(tc.process_date, 'yyyy-MM-dd')             as \"processDate\",\n" +
            "                u.citizenship_no                                   as \"citizenshipNo\",\n" +
            "                vd.vehicle_type                                    as \"vehicleType\",\n" +
            "                vd.identification_no                               as \"identificationNo\",\n" +
            "                tc.paid_amount                                     as \"paidAmount\",\n" +
            "                tc.valid_year                                      as \"validYear\",\n" +
            "                tc.is_verified                                     as \"isVerified\",\n" +
            "                SUBSTRING(tc.paid_bill_doc_url FROM '.*/([^/]+)$') AS \"taxPaidBillImageName\"\n" +
            "from tax_clearance tc\n" +
            "         inner join users u on tc.paid_user_id = u.id\n" +
            "         inner join vehicle_detail vd on tc.vehicle_id = vd.id\n" +
            "where tc.is_verified = false\n" +
            "  and tc.is_rejected = false", nativeQuery = true)
    List<TaxClearanceProjection> getTaxClearanceRequestForAdmin();

    @Query(value = "select distinct tc.valid_year\n" +
            "from tax_clearance tc\n" +
            "         inner join users u on tc.paid_user_id = u.id\n" +
            "         inner join vehicle_detail vd on tc.vehicle_id = vd.id\n" +
            "         left join blue_book bb on bb.vehicle_identification_no = vd.identification_no\n" +
            "where vd.id = ?1\n" +
            "  and is_verified = true\n" +
            "order by tc.valid_year desc\n" +
            "limit 1", nativeQuery = true)
    Integer getValidYear(Integer vehicleId);

    @Query(value = "select u.citizenship_no                      as \"citizenshipNo\",\n" +
            "       string_agg(vd.identification_no, ',') as \"vehicleIdentificationNo\",\n" +
            "       string_agg(bb.number_plate, ',')      as \"numberPlate\"\n" +
            "from users u\n" +
            "         left join blue_book bb on bb.citizenship_no = u.citizenship_no\n" +
            "         left join vehicle_detail vd on vd.identification_no = bb.vehicle_identification_no\n" +
            "where u.id = ?1\n" +
            "group by u.citizenship_no", nativeQuery = true)
    UserCommonDetailProjection getUserDetail(Integer userId);

    @Query(value = "select *\n" +
            "from tax_clearance tc\n" +
            "         inner join vehicle_detail vd on vd.id = tc.vehicle_id\n" +
            "where tc.vehicle_id = ?1 order by tc.id desc limit 1", nativeQuery = true)
    TaxClearance getTaxClearanceByVehicleId(Integer vehicleId);

}
