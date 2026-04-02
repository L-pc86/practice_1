package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("setmeal_dish")
public class SetmealDish {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long setmealId;

    private Long dishId;

    private String name;

    private Double price;

    private Integer copies;
}
