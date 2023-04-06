package com.stone.user.service;

import com.stone.common.bean.BeanCopy;
import com.stone.sdk.user.dto.UserDTO;
import com.stone.sdk.user.service.UserService;
import com.stone.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getById(Long id) {
        if (true) {
            throw new RuntimeException("hhh");
        }
        return BeanCopy.copy(userMapper.selectById(id), UserDTO.class);
    }

    @Override
    public List<UserDTO> listByIds(String ids) {
        return BeanCopy.copyList(userMapper.selectBatchIds(Arrays.asList(ids.split(","))), UserDTO.class);
    }
}
