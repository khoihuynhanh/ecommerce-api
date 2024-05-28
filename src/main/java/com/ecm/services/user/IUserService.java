package com.ecm.services.user;

import com.ecm.dtos.UserDTO;
import com.ecm.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
}
