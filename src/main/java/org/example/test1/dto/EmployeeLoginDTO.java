package org.example.test1.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class EmployeeLoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
