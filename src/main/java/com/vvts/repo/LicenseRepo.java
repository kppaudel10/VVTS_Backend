package com.vvts.repo;

import com.vvts.entity.License;
import com.vvts.projection.LicenseProjection;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select l.id,\n" +
            "       l.citizenship_no as \"citizenshipNo\",\n" +
            "       l.district,\n" +
            "       l.license_no     as \"licenseNo\",\n" +
            "       l.valid_date     as \"validDate\",\n" +
            "       u.name           as \"licensedUserName\"\n" +
            "from license l\n" +
            "         inner join users u on l.citizenship_no = u.citizenship_no\n" +
            "where l.license_no = ?1\n" +
            "   or l.citizenship_no = ?1\n" +
            "   or l.district = ?1\n" +
            "   or ('-1' = ?1)\n" +
            "order by l.id desc", nativeQuery = true)
    List<LicenseProjection> filterLicenseDetails(String searchValue, Pageable pageable);

}
