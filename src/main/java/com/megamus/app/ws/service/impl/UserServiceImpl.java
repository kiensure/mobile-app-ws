/**
 * 
 */
package com.megamus.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.megamus.app.ws.dto.AddressDTO;
import com.megamus.app.ws.dto.UserDTO;
import com.megamus.app.ws.entity.UserEntity;
import com.megamus.app.ws.exceptions.UserServiceException;
import com.megamus.app.ws.model.request.UsersRequestModel;
import com.megamus.app.ws.model.response.ErrorMessages;
import com.megamus.app.ws.repository.UserRepository;
import com.megamus.app.ws.service.UserService;
import com.megamus.app.ws.utils.Const;
import com.megamus.app.ws.utils.Utils;

/**
 * @author mrens
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        // Check email exists
        UserEntity userCheck = userRepository.findByEmail(userDTO.getEmail());
        if (userCheck != null) {
            throw new RuntimeException("Record already exists");
        }

        AddressDTO addressDTO ;
        for(int i = 0; i < userDTO.getAddresses().size(); i++) {
            addressDTO = userDTO.getAddresses().get(i);
            addressDTO.setUserDetails(userDTO);
            addressDTO.setAddressId(utils.generateAddressId(Const.LENGTH_ADDRESS_ID));
            userDTO.getAddresses().set(i, addressDTO);
        }
        
        //BeanUtils.copyProperties(userDTO, userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);

        // Generate public UserId
        String publicUserId = utils.generateUserId(Const.LENGTH_USER_ID);

        userEntity.setUserId(publicUserId);

        // Encrypted password before save into DB
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        // Save User into DB
        UserEntity storedUserDetails = userRepository.save(userEntity);

        //BeanUtils.copyProperties(storedUserDetails, returnUserDTO);

        UserDTO returnUserDTO = modelMapper.map(storedUserDetails, UserDTO.class);

        return returnUserDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDTO getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email + " doesn't exists!!!");
        }

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);

        return userDTO;
    }

    @Override
    public UserDTO getUserById(String userId) {
        UserDTO userDto = new UserDTO();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            // throw new UsernameNotFoundException(userId); -> access denied
            throw new UserServiceException("User doesn't exists");
        }

        //BeanUtils.copyProperties(userEntity, userDto);
        ModelMapper modelMapper = new ModelMapper();
        userDto = modelMapper.map(userEntity, UserDTO.class);

        return userDto;
    }

    @Override
    public UserDTO updateUser(String userId, UserDTO user) {

        // Get user update
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        // Set new value for user
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        // Save user
        UserEntity userUpdate = userRepository.save(userEntity);

        // Response user detail update
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userUpdate, userDTO);

        return userDTO;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDTO> getUsers(UsersRequestModel usersReques) {

        List<UserDTO> lisUserDTOs = new ArrayList<UserDTO>();

        Pageable pageableRequest = PageRequest.of(Integer.valueOf(usersReques.getPage()), 
                                                  Integer.valueOf(usersReques.getPerpage()));

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);

        List<UserEntity> users = usersPage.getContent();

        UserDTO userDto;
        for (UserEntity userEntity : users) {
            userDto = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDto);
            lisUserDTOs.add(userDto);
        }

        return lisUserDTOs;
    }

    @Override
    public List<UserDTO> getTop10ByLastName(String lastName) {
        List<UserDTO> returnList = new ArrayList<UserDTO>();
        
        List<UserEntity> listResult = userRepository.findTop10ByLastname(lastName);
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO;
        for (UserEntity userEntity : listResult) {
            userDTO = new UserDTO();
            modelMapper.map(userEntity, UserDTO.class);
            returnList.add(userDTO);
        }
        return returnList;
    }

}
