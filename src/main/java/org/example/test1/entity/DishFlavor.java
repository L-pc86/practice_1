package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("dish_flavor")
public class DishFlavor {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long dishId;

    private String name;

    private String value;
}
