package com.stone.goods.controller;

import com.stone.goods.client.UserServiceClient;
import com.stone.sdk.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/goodsService")
public class GoodsController {

    @Autowired
    private UserServiceClient userServiceClient;

    @RequestMapping("/get/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        UserDTO user = userServiceClient.getById(id);
        if (Objects.nonNull(user)) {
            return user;
        }
        return UserDTO.defaultUser();
    }
}
