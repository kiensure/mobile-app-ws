/**
 * 
 */
package com.megamus.app.ws.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.megamus.app.ws.entity.UserEntity;

/**
 * @author mrens
 *
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    List<UserEntity> findTop10ByLastname(String lastName);
}
