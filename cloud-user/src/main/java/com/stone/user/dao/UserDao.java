package com.stone.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stone.user.pojo.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserDO> {

}
