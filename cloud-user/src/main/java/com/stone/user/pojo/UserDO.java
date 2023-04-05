package com.stone.user.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "user")
@Data
public class UserDO implements Serializable {

    private static final long serialVersionUID = 8403961952871095695L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

}
