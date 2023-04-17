package com.vvts.repo;

import com.vvts.entity.AdministrativeUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auther kul.paudel
 * @created at 2023-04-17
 */

public interface AdministrativeUserRepo extends JpaRepository<AdministrativeUser,Integer> {

}
