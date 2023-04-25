package com.vvts.repo;

import com.vvts.entity.License;
import com.vvts.projection.LicenseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
@Repository
public interface LicenseRepo extends JpaRepository<License, Integer> {

    @Query(value = "select count(id) from license where license_no = ?1", nativeQuery = true)
    Integer getCountOfLicenseByLicenseNumber(String licenseNumber);

    @Query(value = "select count(id) from license where citizenship_no = ?1", nativeQuery = true)
    Integer getCitizenshipNumberCount(String citizenshipNo);

    @Query(value = "select id, citizenship_no as \"citizenshipNo\", district, license_no as \"licenseNo\", valid_date as \"validDate\"\n" +
            "from license\n" +
            "order by id desc", nativeQuery = true)
    List<LicenseProjection> getLicenseDetailList();

    @Query(value = "select id, citizenship_no as \"citizenshipNo\", district, license_no as \"licenseNo\", valid_date as \"validDate\"\n" +
            "from license\n" +
            "where license_no = ?1\n" +
            "   or citizenship_no = ?2 or valid_date = to_date(?3,'yyyy-MM-dd') or district = ?4\n" +
            "order by id desc", nativeQuery = true)
    List<LicenseProjection> filterLicenseDetails(String licenseNo, String citizenshipNo, String validDate,String district);

}
