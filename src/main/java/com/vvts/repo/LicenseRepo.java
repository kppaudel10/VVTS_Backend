package com.vvts.repo;

import com.vvts.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */
public interface LicenseRepo extends JpaRepository<License,Integer> {

}
