package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("shop")
public class Shop {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer status;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
