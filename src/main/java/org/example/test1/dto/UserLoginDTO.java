package org.example.test1.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserLoginDTO {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    private String code;
}
