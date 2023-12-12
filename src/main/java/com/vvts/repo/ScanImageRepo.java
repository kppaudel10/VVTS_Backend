package com.vvts.repo;

import com.vvts.entity.ScanImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-06-15
 */
public interface ScanImageRepo extends JpaRepository<ScanImage, Integer> {

    @Query(value = "select count(id) from scan_image where scan_image_name = ?1", nativeQuery = true)
    Integer countScanImageName(String scanImageName);
    @Query(value = "select * from scan_image where scan_image_hash_value = ?1 order by id desc limit 1", nativeQuery = true)
    ScanImage getScanImageByScanImageHasValue(String imageHashValue);

}
