package com.stone.user.controller;

import com.stone.common.cache.redis.RedisService;
import com.stone.common.exception.GlobalException;
import com.stone.common.result.ApiResult;
import com.stone.common.result.Message;
import com.stone.dto.UserDTO;
import com.stone.sdk.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
            throw new GlobalException(Message.PARAM_EXCEPTION);
        }
        result.setData(user);
        return result;
    }

    @PostMapping("/user/update")
    public ApiResult<Boolean> update(UserDTO user) {
        ApiResult<Boolean> result = new ApiResult<>();
        if (Objects.isNull(user) || Objects.isNull(user.getId())) {
            throw new GlobalException(Message.PARAM_EXCEPTION);
        }
        result.setData(userService.updateById(user) > 0 ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

}
