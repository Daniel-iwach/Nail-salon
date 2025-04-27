package com.chamisnails.nailsalon.services.interfaces;


import com.chamisnails.nailsalon.presentation.dto.*;

import java.util.List;


public interface IUserService {
    UserDTO getByUsername(String username);

    UserDTO save(UserDTO user);

    UserDTO getUserById(String userId);

    String getUserId();

    List<UserDTO>getAll();
}