package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("setmeal")
public class Setmeal {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String description;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(exist = false)
    private List<SetmealDish> setmealDishes;

    @TableField(exist = false)
    private String categoryName;
}
