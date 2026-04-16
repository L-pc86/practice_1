package org.example.test1.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SetmealDishDTO {

    private Long id;

    private Long setmealId;

    private Long dishId;

    private String name;

    private BigDecimal price;

    private Integer copies;
}
