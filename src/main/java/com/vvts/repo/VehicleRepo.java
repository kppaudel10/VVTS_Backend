package com.vvts.repo;

import com.vvts.entity.VehicleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleRepo extends JpaRepository<VehicleDetail, Integer> {

}
