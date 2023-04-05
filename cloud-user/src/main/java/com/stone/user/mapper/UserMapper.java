package com.stone.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stone.user.pojo.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}
