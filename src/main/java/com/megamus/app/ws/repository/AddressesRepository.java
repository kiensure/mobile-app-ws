/**
 * 
 */
package com.megamus.app.ws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.megamus.app.ws.entity.AddressEntity;
import com.megamus.app.ws.entity.UserEntity;

/**
 * @author mrens
 *
 */
@Repository
public interface AddressesRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

    AddressEntity findByAddressId(String addressId);
}
