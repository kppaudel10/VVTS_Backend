package com.vvts.repo;

import com.vvts.entity.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-05-26
 */
public interface PinCodeRepo extends JpaRepository<PinCode, Integer> {

    @Query(value = "select pc.pin_code from pin_code pc where pc.user_id = ?1", nativeQuery = true)
    String getPinCodeByUserId(Integer loginUserId);

}
