/**
 * 
 */
package com.megamus.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megamus.app.ws.dto.AddressDTO;
import com.megamus.app.ws.entity.AddressEntity;
import com.megamus.app.ws.entity.UserEntity;
import com.megamus.app.ws.repository.AddressesRepository;
import com.megamus.app.ws.repository.UserRepository;
import com.megamus.app.ws.service.AddressesService;

/**
 * @author mrens
 *
 */
@Service
public class AddressesServiceImpl implements AddressesService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressesRepository addressesRepository;

    @Override
    public List<AddressDTO> getAddresses(String userId) {
        List<AddressDTO> returnValue = new ArrayList<AddressDTO>();

        UserEntity userEntity = userRepository.findByUserId(userId);

        ModelMapper modelMapper = new ModelMapper();

        if (userEntity == null) {
            return returnValue;
        }

        Iterable<AddressEntity> addresses = addressesRepository.findAllByUserDetails(userEntity);

        for (AddressEntity addressEntity : addresses) {
            returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
        }

        return null;
    }

    @Override
    public AddressDTO getAddress(String addressId) {
        AddressDTO returnValue = new AddressDTO();

        AddressEntity entity = addressesRepository.findByAddressId(addressId);

        if (entity != null) {
            ModelMapper modelMapper = new ModelMapper();
            returnValue = modelMapper.map(entity, AddressDTO.class);
        }

        return returnValue;
    }

}
