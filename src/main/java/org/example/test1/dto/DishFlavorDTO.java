package org.example.test1.dto;

import lombok.Data;

@Data
public class DishFlavorDTO {

    private Long id;

    private Long dishId;

    private String name;

    private String value;
}
