package com.stone.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3344463638805468347L;

    private Long id;

    private String name;

    private Integer age;

    private Integer type;
}
