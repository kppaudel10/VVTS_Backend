package com.vvts.repo;

import com.vvts.entity.VehicleDetail;
import com.vvts.projection.NumberPlateScannerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

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


    @Query(value = "select u.id                         as userId,\n" +
            "       u.name                       as name,\n" +
            "       u.mobile_number              as contact,\n" +
            "       u.email                      as email,\n" +
            "       u.address,\n" +
            "       u.citizenship_no             as citizenshipNo,\n" +
            "       u.profile_image_url          as profileImageUrl,\n" +
            "       l.license_no                 as licenseNo,\n" +
            "       l.valid_date                 as licenseValidDate,\n" +
            "       case\n" +
            "           when to_char(l.valid_date, 'yyyy-MM-dd') > to_char(now(), 'yyyy-MM-ddd') then\n" +
            "               true\n" +
            "           else false end           as \"isLicenseValid\",\n" +
            "       bb.effective_date            as \"blueBookEffectiveDate\",\n" +
            "       bb.vehicle_identification_no as \"vehicleIdentificationNo\",\n" +
            "       vd.manufacture_year          as \"manufactureYear\",\n" +
            "       vd.company_name              as \"vehicleCompanyName\"\n" +
            "\n" +
            "\n" +
            "from blue_book bb\n" +
            "         inner join users u on bb.citizenship_no = u.citizenship_no\n" +
            "         left join license l on l.citizenship_no = u.citizenship_no\n" +
            "         left join vehicle_detail vd on vd.identification_no = bb.vehicle_identification_no\n" +
            "where bb.number_plate = ?1\n" +
            "limit 1", nativeQuery = true)
    NumberPlateScannerProjection getUserAndVehicleDetailByNumberPlate(String scanOutput);

    VehicleDetail getVehicleDetailByVehicleIdentificationNo(String vehicleIdentificationNo);

    @Query(value = "select company_code,\n" +
            "       company_name,\n" +
            "       manufacture_year,\n" +
            "       vehicle_type,\n" +
            "       identification_no\n" +
            "from vehicle_detail\n" +
            "where vendor_id = ?1 order by id desc", nativeQuery = true)
    List<Map<String, Object>> getVehicleList(int vendorId);


    @Query(value = "select u.id                         as userId,\n" +
            "       u.name                       as name,\n" +
            "       u.mobile_number              as contact,\n" +
            "       u.email                      as email,\n" +
            "       u.address,\n" +
            "       u.citizenship_no             as citizenshipNo,\n" +
            "       u.profile_image_url          as profileImageUrl,\n" +
            "       l.license_no                 as licenseNo,\n" +
            "       l.valid_date                 as licenseValidDate,\n" +
            "       case\n" +
            "           when to_char(l.valid_date, 'yyyy-MM-dd') > to_char(now(), 'yyyy-MM-ddd') then\n" +
            "               true\n" +
            "           else false end           as \"isLicenseValid\",\n" +
            "       bb.effective_date            as \"blueBookEffectiveDate\",\n" +
            "       bb.vehicle_identification_no as \"vehicleIdentificationNo\",\n" +
            "       vd.manufacture_year          as \"manufactureYear\",\n" +
            "       vd.company_name              as \"vehicleCompanyName\"\n" +
            "\n" +
            "\n" +
            "from blue_book bb\n" +
            "         inner join users u on bb.citizenship_no = u.citizenship_no\n" +
            "         left join license l on l.citizenship_no = u.citizenship_no\n" +
            "         left join vehicle_detail vd on vd.identification_no = bb.vehicle_identification_no\n" +
            "where u.id = ?1\n" +
            "limit 1", nativeQuery = true)
    NumberPlateScannerProjection getUserAndVehicleDetailByUserId(Integer userId);



}
