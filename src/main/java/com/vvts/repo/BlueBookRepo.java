package com.vvts.repo;

import com.vvts.entity.BlueBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface BlueBookRepo extends JpaRepository<BlueBook, Integer> {

    @Query(value = "select *\n" +
            "from blue_book\n" +
            "where citizenship_no = ?1 and effective_date = to_date(?2,'yyyy-MM-dd')\n" +
            "  and vehicle_identification_no = ?3  and vehicle_type = ?4 limit 1", nativeQuery = true)
    BlueBook getDuplicateDataCount(String citizenshipNo, String effectiveDate, String identificationNo, Integer vehicleType);

}
