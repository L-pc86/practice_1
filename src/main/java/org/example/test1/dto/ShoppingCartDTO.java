package org.example.test1.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ShoppingCartDTO {

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    @NotNull(message = "数量不能为空")
    private Integer number;
}
