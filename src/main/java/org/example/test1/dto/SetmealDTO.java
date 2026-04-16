package org.example.test1.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SetmealDTO {

    private Long id;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private String name;

    private BigDecimal price;

    private String image;

    private String description;

    private Integer status;

    private List<SetmealDishDTO> setmealDishes;
}
