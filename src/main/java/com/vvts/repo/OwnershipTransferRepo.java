package com.vvts.repo;

import com.vvts.entity.OwnershipTransfer;
import com.vvts.projection.BuyRequestProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-28
 */
public interface OwnershipTransferRepo extends JpaRepository<OwnershipTransfer, Integer> {

    @Query(value = "select distinct ot.id,\n" +
            "       ot.is_approve_by_admin       as \"isApproveByAdmin\",\n" +
            "       ot.is_approve_by_owner       as \"isApproveByOwner\",\n" +
            "       ot.approve_date              as \"approveDate\",\n" +
            "       ot.request_date              as \"requestDate\",\n" +
            "       ot.vehicle_identification_no as \"vehicleIdentificationNo\",\n" +
            "       vd.company_code           as \"companyCode\",\n" +
            "       vd.manufacture_year       as \"manufactureYear\",\n" +
            "       vd.vehicle_type           as \"vehicleType\",\n" +
            "       u.name                    as \"buyerName\",\n" +
            "       u.mobile_number           as \"buyerMobileNumber\",\n" +
            "       u.profile_image_url       as \"buyerProfileUrl\",\n" +
            "       u.citizenship_no          as \"buyerCitizenshipNo\",\n" +
            "       u.citizenship_font_url    as \"buyerCitizenshipFontUrl\",\n" +
            "       u.citizenship_back_url    as \"buyerCitizenshipBackUrl\"\n" +
            "from ownership_transfer ot\n" +
            "         inner join users u on ot.buyer_id = u.id\n" +
            "         inner join vehicle_detail vd on u.id = vd.vendor_id\n" +
            "where ot.seller_id = ?1", nativeQuery = true)
    List<BuyRequestProjection> getOwnershipTransferByOwnerId(Integer ownerId);


    @Query(value = "select count(vd.id)\n" +
            "from ownership_transfer ot\n" +
            "         inner join vehicle_detail vd on ot.vehicle_id = vd.id\n" +
            "where vd.identification_no = ?1\n" +
            "  and ot.buyer_id = ?2\n" +
            "  and ot.buyer_id = ?3", nativeQuery = true)
    Integer getCountBuyRequest(String vehicleNo, Integer buyerId, Integer sellerId);
}
