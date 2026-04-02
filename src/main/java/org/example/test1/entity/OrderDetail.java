package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("order_detail")
public class OrderDetail {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long dishId;

    private Long setmealId;

    private String name;

    private String image;

    private BigDecimal price;

    private Integer copies;
}
