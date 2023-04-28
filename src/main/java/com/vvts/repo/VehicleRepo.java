package com.vvts.repo;

import com.vvts.entity.VehicleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-04-25
 */
public interface VehicleRepo extends JpaRepository<VehicleDetail, Integer> {

    @Query(value = "select count(id) from vehicle_detail where company_code = ?1", nativeQuery = true)
    Integer getCompanyCodeCount(String companyCode);

    @Query(value = "select * from vehicle_detail where company_code = ?1 and vehicle_type = ?2 order by id desc limit 1", nativeQuery = true)
    VehicleDetail getVehicleDetailByCompanyCodeAndVehicleType(String companyCode, Integer vehicleType);

    @Query(value = "select count(id) from vehicle_detail where identification_no = ?1", nativeQuery = true)
    Integer getCountByVIN(String identificationNo);

    @Query(value = "select count(vd.id)\n" +
            "from vehicle_detail vd\n" +
            "         inner join blue_book bb on vd.identification_no = bb.vehicle_identification_no\n" +
            "where vd.identification_no = ?1\n" +
            "  and bb.citizenship_no = ?2\n" +
            "group by bb.effective_date\n" +
            "order by bb.effective_date desc limit 1", nativeQuery = true)
    Integer getVehicleDetailByVINAndNumber(String vin, String citizenshipNo);


}
