package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNumber;

    private Long userId;

    private Long addressId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private String username;

    private String phone;

    private String address;

    private String consignee;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
