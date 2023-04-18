package com.vvts.repo;

import com.vvts.entity.AdministrativeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */

@Repository
public interface AdministrativeUserRepo extends JpaRepository<AdministrativeUser, Integer> {
    AdministrativeUser getAdministrativeUserByUserName(String userName);

}
