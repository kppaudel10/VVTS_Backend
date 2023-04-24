package com.vvts.repo;

import com.vvts.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @auther kul.paudel
 * @created at 2023-04-22
 */
public interface AccessTokenRepo extends JpaRepository<AccessToken, Integer> {

    @Query(value = "select * from access_token where user_name = ?1", nativeQuery = true)
    AccessToken getAccessTokenExistsOrNot(String userName);

    @Query(value = "select * from access_token where token = ?1", nativeQuery = true)
    AccessToken getAccessTokenExists(String token);


    @Modifying
    @Query(value = "delete from access_token where user_name = ?1", nativeQuery = true)
    void deleteAccessTokenByUserName(String userName);

}
