package com.stone.user.service;

import com.stone.common.bean.BeanCopy;
import com.stone.dto.UserDTO;
import com.stone.sdk.UserService;
import com.stone.user.dao.UserDao;
import com.stone.user.pojo.UserDO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;

@DubboService
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Cacheable(cacheNames = "cloud_cache_getUserById", key = "#id")
    @Override
    public UserDTO getUserById(Long id) {
        return BeanCopy.doConvertDto(userDao.selectById(id), UserDTO.class);
    }

    @CacheEvict(cacheNames = "cloud_cache_getUserById", key = "#user.id")
    @Override
    public int updateById(UserDTO user) {
        user.setGmtModified(new Date());
        return userDao.updateById(BeanCopy.dtoConvertDo(user, UserDO.class));
    }
}
