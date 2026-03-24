package org.example.test1.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@TableName("org")
public class Org {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "组织名称不能为空")
    private String name;

    private String code;

    private String description;

    private Integer parentId;

    private Integer sort;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
