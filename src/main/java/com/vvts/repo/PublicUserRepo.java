package com.vvts.repo;

import com.vvts.entity.PublicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @auther kul.paudel
 * @created at 2023-04-08
 */

@Repository
public interface PublicUserRepo extends JpaRepository<PublicUser,Integer> {
}
