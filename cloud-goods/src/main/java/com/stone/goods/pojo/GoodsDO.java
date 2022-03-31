package com.stone.goods.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("goods")
public class GoodsDO implements Serializable {

    private static final long serialVersionUID = -3389881641503421443L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


}
