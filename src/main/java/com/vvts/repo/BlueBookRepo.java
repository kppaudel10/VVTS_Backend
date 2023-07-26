package com.vvts.repo;

import com.vvts.entity.BlueBook;
import com.vvts.projection.BlueBookProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    @Query(value = "select bb.id,\n" +
            "       bb.citizenship_no            as \"citizenshipNo\",\n" +
            "       bb.effective_date            as \"effectiveDate\",\n" +
            "       bb.vehicle_identification_no as \"vehicleIdentificationNo\",\n" +
            "       bb.vehicle_type              as \"vehicleType\",\n" +
            "       u.name                       as \"currentBlueBookOwnUserName\",\n" +
            "       u.mobile_number              as \"contact\",\n" +
            "       bb.number_plate              as \"numberPlate\"\n" +
            "from blue_book bb\n" +
            "         inner join users u on bb.citizenship_no = u.citizenship_no\n" +
            "where bb.citizenship_no like ?1\n" +
            "   or lower(bb.vehicle_identification_no) like lower(?1)\n" +
            "   or ('--1' like ?1) order by bb.id desc", nativeQuery = true)
    List<BlueBookProjection> getBlueBookData(String searchData);

    @Query(value = "select count(id) from blue_book where number_plate = ?1", nativeQuery = true)
    Integer getCountNumberPlateExits(String numberPlate);

    BlueBook getBlueBookByVehicleIdentificationNo(String identificationNo);


}
