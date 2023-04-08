package com.vvts.repo;

import com.vvts.entity.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */
public interface PublicUserRepo extends JpaRepository<PublicUser,Integer> {

}
