package com.vvts.repo;

import com.vvts.entity.Users;
import com.vvts.projection.UserDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users getUsersByMobileNumber(String mobileNumber);

    @Query(value = "select count(id) from users where mobile_number = ?1", nativeQuery = true)
    Integer getMobileNumberCount(String mobileNumber);

    @Query(value = "select count(id) from users where email = ?1", nativeQuery = true)
    Integer getEmailCount(String email);

    @Query(value = "select id                   as \"userId\",\n" +
            "       address,\n" +
            "       email,\n" +
            "       mobile_number        as \"mobileNumber\",\n" +
            "       name,\n" +
            "       citizenship_no       as \"citizenshipNo\",\n" +
            "       profile_image_url    as \"profilePictureUrl\",\n" +
            "       citizenship_font_url as \"citizenshipFontUrl\",\n" +
            "       citizenship_back_url as \"citizenshipBackUrl\"\n" +
            "from users\n" +
            "where is_enable = false\n" +
            "  and is_new_kyc_request = true", nativeQuery = true)
    List<UserDetailProjection> getNewKycRequestUserData();

    @Query(value = "select count(id) from users where citizenship_no = ?1 and is_enable = true", nativeQuery = true)
    Integer getVerifiedCitizenshipCount(String citizenshipNo);


}
