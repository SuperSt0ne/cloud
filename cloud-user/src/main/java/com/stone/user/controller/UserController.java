package com.stone.user.controller;

import com.stone.sdk.user.dto.UserDTO;
import com.stone.sdk.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userService")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/get/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getById(id);
    }

    @RequestMapping("/list")
    public List<UserDTO> list(String ids) {
        return userService.listByIds(ids);
    }
}
