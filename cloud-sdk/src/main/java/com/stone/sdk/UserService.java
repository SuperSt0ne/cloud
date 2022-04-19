package com.stone.sdk;

import com.stone.dto.UserDTO;
import org.springframework.cache.annotation.Cacheable;

public interface UserService {

    UserDTO getUserById(Long id);

    int updateById(UserDTO user);

}
