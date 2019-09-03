/**
 * 
 */
package com.megamus.app.ws.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.megamus.app.ws.dto.AddressDTO;
import com.megamus.app.ws.dto.UserDTO;
import com.megamus.app.ws.exceptions.UserServiceException;
import com.megamus.app.ws.model.request.UserDetailsRequestModel;
import com.megamus.app.ws.model.request.UsersRequestModel;
import com.megamus.app.ws.model.response.AddressRest;
import com.megamus.app.ws.model.response.ErrorMessages;
import com.megamus.app.ws.model.response.OperationStatusModel;
import com.megamus.app.ws.model.response.ResponseOperationName;
import com.megamus.app.ws.model.response.ResponseOperationStatus;
import com.megamus.app.ws.model.response.UserRest;
import com.megamus.app.ws.service.AddressesService;
import com.megamus.app.ws.service.UserService;

/**
 * @author mrens
 *
 */
@RestController
@RequestMapping("users") // localhost:8080/users
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressesService addressesService;

    @GetMapping(path = "/{id}", 
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public UserRest getUser(@PathVariable("id") String userId) {
        UserRest userRest = new UserRest();

        UserDTO UserDTO = userService.getUserById(userId);

        //BeanUtils.copyProperties(UserDTO, userRest);
        ModelMapper modelMapper = new ModelMapper();
        userRest = modelMapper.map(UserDTO, UserRest.class);
        return userRest;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
                 produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        // DTO using to create user
        // UserDTO userDTO = new UserDTO();

        // Copy properties from request to DTO
        //BeanUtils.copyProperties(userDetails, userDTO);

        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);

        // Create User
        UserDTO createUser = userService.createUser(userDTO);

        // Object response
        UserRest userResponse = new UserRest();

        // Copy properties from result to response
        //BeanUtils.copyProperties(createUser, userResponse);
        userResponse = modelMapper.map(createUser, UserRest.class);
        return userResponse;
    }

    @PutMapping(path = "/{id}", 
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, 
                               @PathVariable("id") String userId) {

        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(userDetails, userDTO);

        UserDTO updateUser = userService.updateUser(userId, userDTO);

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(updateUser, userRest);

        return userRest;
    }

    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public OperationStatusModel deleteUser(@PathVariable("id") String userId) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(ResponseOperationName.DELETE.name());

        userService.deleteUser(userId);

        returnValue.setOperationStatus(ResponseOperationStatus.SUCCESS.name());
        return returnValue;
    }

    /**
     * Get list user
     * @param usersRequest
     * @return
     */
    @GetMapping
    public List<UserRest> getUsers(@RequestBody UsersRequestModel usersRequest) {

        List<UserRest> listReturn = new ArrayList<UserRest>();

        List<UserDTO> users = userService.getUsers(usersRequest);

        UserRest userRest;

        for (UserDTO userDTO : users) {
            userRest = new UserRest();
            BeanUtils.copyProperties(userDTO, userRest);
            listReturn.add(userRest);
        }

        return listReturn;
    }

    @GetMapping(path = "/{id}/addresses", 
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressRest> getUserAddresses(@PathVariable("id") String userId){
        List<AddressRest> listReturn = new ArrayList<AddressRest>();

        List<AddressDTO> listAddressDTOs = addressesService.getAddresses(userId);

        ModelMapper modelMapper = new ModelMapper();
        if(listAddressDTOs != null && listAddressDTOs.size() > 0) {
            Type listType = new TypeToken<List<AddressRest>>() {}.getType();
            listReturn = modelMapper.map(listAddressDTOs, listType);
        }

        return listReturn;
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}", 
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public AddressRest getUserAddress(@PathVariable("addressId") String addressId) {
        AddressDTO addressDTO = addressesService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        AddressRest returnValue = modelMapper.map(addressDTO, AddressRest.class);

        return returnValue;
    }
}
