package com.vvts.repo;

import com.vvts.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
@Repository
public interface LicenseRepo extends JpaRepository<License,Integer> {

    @Query(value = "select count(id) from license where license_no = ?1",nativeQuery = true)
    Integer getCountOfLicenseByLicenseNumber(String licenseNumber);

}
