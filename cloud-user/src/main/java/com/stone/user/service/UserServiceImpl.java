package com.stone.user.service;

import com.stone.user.dao.UserDao;
import com.stone.common.bean.BeanCopy;
import com.stone.dto.UserDTO;
import com.stone.sdk.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDTO getUserById(Long id) {
        return BeanCopy.doConvertDto(userDao.selectById(id), UserDTO.class);
    }
}
