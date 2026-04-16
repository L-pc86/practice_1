package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("shopping_cart")
public class ShoppingCart {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    private String name;

    private String image;

    private BigDecimal price;

    private Integer number;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
