package com.stone.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3344463638805468347L;

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String photo;

    private String password;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer status;

    private Integer isDelete;
}
