package org.example.test1.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;

    @TableField(value = "name")
    @NotBlank(message = "用户名不能为空")
    private String name;

    @TableField(value = "age")
    @NotNull(message = "年龄不能为空")
    private Integer age;

    @TableField(value = "email")
    private String email;

    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
