package org.example.test1.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableId(value = "id" , type = IdType.AUTO)
    private Integer id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "email")
    private String email;

    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;
}
