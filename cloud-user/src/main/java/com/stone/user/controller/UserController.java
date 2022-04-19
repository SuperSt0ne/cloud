package com.stone.user.controller;

import com.alibaba.fastjson.JSON;
import com.stone.common.cache.redis.RedisService;
import com.stone.common.exception.GlobalException;
import com.stone.common.result.ApiResult;
import com.stone.common.result.Message;
import com.stone.dto.UserDTO;
import com.stone.sdk.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @GetMapping("/user/{userId}")
    public ApiResult<UserDTO> user(@PathVariable Long userId) {
        ApiResult<UserDTO> result = new ApiResult<>();
        if (Objects.isNull(userId)) {
            result.setErrorMsg(Message.PARAM_EXCEPTION);
            return result;
        }
        UserDTO user = userService.getUserById(userId);
        if (Objects.isNull(user)) {
            result.setErrorMsg(Message.PARAM_EXCEPTION);
            return result;
        }
        result.setData(user);
        return result;
    }

}
