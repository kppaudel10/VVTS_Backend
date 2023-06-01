package com.vvts.repo;

import com.vvts.entity.OwnershipTransfer;
import com.vvts.projection.BuyRequestProjection;
import com.vvts.projection.BuyerRequestProjection;
import com.vvts.projection.OwnershipRequestProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-28
 */
public interface OwnershipTransferRepo extends JpaRepository<OwnershipTransfer, Integer> {

    @Query(value = "select distinct ot.id,\n" +
            "                ot.is_approve_by_admin as \"isApproveByAdmin\",\n" +
            "                ot.is_approve_by_owner as \"isApproveByOwner\",\n" +
            "                ot.approve_date        as \"approveDate\",\n" +
            "                ot.request_date        as \"requestDate\",\n" +
            "                vd.identification_no   as \"vehicleIdentificationNo\",\n" +
            "                vd.company_code        as \"companyCode\",\n" +
            "                vd.manufacture_year    as \"manufactureYear\",\n" +
            "                vd.vehicle_type        as \"vehicleType\",\n" +
            "                u.name                 as \"buyerName\",\n" +
            "                u.email                as \"buyerEmail\",\n" +
            "                u.mobile_number        as \"buyerMobileNumber\",\n" +
            "                u.address              as \"buyerAddress\",\n" +
            "                u.profile_image_url    as \"buyerProfileUrl\",\n" +
            "                u.citizenship_no       as \"buyerCitizenshipNo\",\n" +
            "                u.citizenship_font_url as \"buyerCitizenshipFontUrl\",\n" +
            "                u.citizenship_back_url as \"buyerCitizenshipBackUrl\"\n" +
            "from ownership_transfer ot\n" +
            "         inner join users u on ot.buyer_id = u.id\n" +
            "         inner join vehicle_detail vd on vd.id = ot.vehicle_id\n" +
            "where ot.seller_id = ?1\n" +
            "  and ot.is_approve_by_owner = false\n" +
            "  and ot.status = 1", nativeQuery = true)
    List<BuyRequestProjection> getOwnershipTransferByOwnerId(Integer ownerId);


    @Query(value = "select count(vd.id)\n" +
            "from ownership_transfer ot\n" +
            "         inner join vehicle_detail vd on ot.vehicle_id = vd.id\n" +
            "where vd.identification_no = ?1\n" +
            "  and ot.buyer_id = ?2\n" +
            "  and ot.buyer_id = ?3", nativeQuery = true)
    Integer getCountBuyRequest(String vehicleNo, Integer buyerId, Integer sellerId);


    @Query(value = "select u.name               as \"ownerName\",\n" +
            "       u.mobile_number      as \"ownerMobileNo\",\n" +
            "       vd.identification_no as \"identificationNo\",\n" +
            "       vd.vehicle_type      as \"vehicleType\",\n" +
            "       ot.request_date      as \"requestDate\",\n" +
            "       case\n" +
            "           when ot.is_approve_by_owner = false and status = 1 then 'Pending on Owner'\n" +
            "           when ot.is_approve_by_admin = false and ot.is_approve_by_owner = true and status = 1 then 'Pending on Owner'\n" +
            "           when status = 0 then 'Rejected'\n" +
            "           end              as \"requestStatus\"\n" +
            "from ownership_transfer ot\n" +
            "         inner join vehicle_detail vd on ot.vehicle_id = vd.id\n" +
            "         inner join users u on ot.seller_id = u.id\n" +
            "where ot.buyer_id = ?1 and status = 1", nativeQuery = true)
    List<BuyerRequestProjection> getBuyRequestByLoginUser(Integer loginUserId);

    @Modifying
    @Query(value = "update ownership_transfer set status = ?1 , is_approve_by_owner = ?2 where id = ?3", nativeQuery = true)
    void updateOwnerActionOnOwnershipRequest(Integer activeStatus, boolean status, Integer id);

    @Modifying
    @Query(value = "update ownership_transfer set status = ?1, is_approve_by_admin = ?2 where id = ?3", nativeQuery = true)
    void updateAdminActionOnOwnershipRequest(Integer activeStatus, boolean status, Integer id);


    @Query(nativeQuery = true, value = "with buyerDetail as (select distinct ot.id,\n" +
            "                                     ot.request_date        as \"requestDate\",\n" +
            "                                     vd.identification_no   as \"vehicleIdentificationNo\",\n" +
            "                                     vd.company_code        as \"companyCode\",\n" +
            "                                     vd.manufacture_year    as \"manufactureYear\",\n" +
            "                                     vd.vehicle_type        as \"vehicleType\",\n" +
            "                                     u.name                 as \"buyerName\",\n" +
            "                                     u.email                as \"buyerEmail\",\n" +
            "                                     u.mobile_number        as \"buyerMobileNumber\",\n" +
            "                                     u.address              as \"buyerAddress\",\n" +
            "                                     u.profile_image_url    as \"buyerProfileUrl\",\n" +
            "                                     u.citizenship_no       as \"buyerCitizenshipNo\",\n" +
            "                                     u.citizenship_font_url as \"buyerCitizenshipFontUrl\",\n" +
            "                                     u.citizenship_back_url as \"buyerCitizenshipBackUrl\"\n" +
            "                     from ownership_transfer ot\n" +
            "                              inner join users u on ot.buyer_id = u.id\n" +
            "                              inner join vehicle_detail vd on vd.id = ot.vehicle_id\n" +
            "                         and ot.is_approve_by_owner = true\n" +
            "                         and ot.status = 1),\n" +
            "     sellerDetail as (select distinct ot.id as otId,\n" +
            "                                      u.name                 as \"sellerName\",\n" +
            "                                      u.email                as \"sellerEmail\",\n" +
            "                                      u.mobile_number        as \"sellerMobileNumber\",\n" +
            "                                      u.address              as \"sellerAddress\",\n" +
            "                                      u.profile_image_url    as \"sellerProfileUrl\",\n" +
            "                                      u.citizenship_no       as \"sellerCitizenshipNo\",\n" +
            "                                      u.citizenship_font_url as \"sellerCitizenshipFontUrl\",\n" +
            "                                      u.citizenship_back_url as \"sellerCitizenshipBackUrl\"\n" +
            "                      from ownership_transfer ot\n" +
            "                               inner join users u on ot.seller_id = u.id\n" +
            "                          and ot.is_approve_by_owner = true\n" +
            "                          and ot.status = 1)\n" +
            "select * from buyerDetail bd\n" +
            "inner join sellerDetail sd on bd.id = sd.otId")
    List<OwnershipRequestProjection> getOwnershipTransferList();
}
