package com.stone.sdk.user.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private Long id;

    private String name;

    public static UserDTO defaultUser() {
        UserDTO user = new UserDTO();
        user.setId(0L);
        user.setName("default");
        return user;
    }
}
