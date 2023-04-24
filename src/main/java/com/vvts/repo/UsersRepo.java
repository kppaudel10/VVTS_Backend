package com.vvts.repo;

import com.vvts.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users getUsersByMobileNumber(String mobileNumber);

    @Query(value = "select count(id) from users where mobile_number = ?1", nativeQuery = true)
    Integer getMobileNumberCount(String mobileNumber);

    @Query(value = "select count(id) from users where email = ?1", nativeQuery = true)
    Integer getEmailCount(String email);

}
