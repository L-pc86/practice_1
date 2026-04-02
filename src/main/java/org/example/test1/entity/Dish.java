package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dish")
public class Dish {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "菜品名称不能为空")
    @Size(max = 50, message = "菜品名称长度不能超过50个字符")
    private String name;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotBlank(message = "图片不能为空")
    private String image;

    private String description;

    private Integer sort;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
