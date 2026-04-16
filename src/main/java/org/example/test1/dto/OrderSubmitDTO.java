package org.example.test1.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderSubmitDTO {

    @NotNull(message = "地址ID不能为空")
    private Long addressId;

    private Integer payMethod;

    private String remark;
}
