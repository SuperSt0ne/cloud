package com.stone.sdk.user.service;

import com.stone.sdk.user.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO getById(Long id);

    List<UserDTO> listByIds(String ids);

}
