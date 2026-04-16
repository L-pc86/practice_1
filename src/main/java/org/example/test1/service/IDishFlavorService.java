package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.DishFlavor;
import java.util.List;

public interface IDishFlavorService extends IService<DishFlavor> {

    List<DishFlavor> listByDishId(Long dishId);

    void saveBatch(List<DishFlavor> flavors);

    void deleteByDishId(Long dishId);

    void deleteByDishIds(List<Long> dishIds);
}
