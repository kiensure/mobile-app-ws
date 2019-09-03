/**
 * 
 */
package com.megamus.app.ws.service;

import java.util.List;

import com.megamus.app.ws.dto.AddressDTO;

/**
 * @author mrens
 *
 */
public interface AddressesService {
    List<AddressDTO> getAddresses(String userId);

    AddressDTO getAddress(String addressId);
}
