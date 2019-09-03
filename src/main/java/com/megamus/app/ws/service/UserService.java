/**
 * 
 */
package com.megamus.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.megamus.app.ws.dto.UserDTO;
import com.megamus.app.ws.model.request.UsersRequestModel;

/**
 * @author mrens
 *
 */
public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUser(String email);

    UserDTO getUserById(String userId);

    UserDTO updateUser(String userId, UserDTO user);

    void deleteUser(String userId);

    List<UserDTO> getUsers(UsersRequestModel usersReques);

    List<UserDTO> getTop10ByLastName(String lastName);
}
