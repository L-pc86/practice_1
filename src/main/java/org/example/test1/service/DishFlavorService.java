package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.DishFlavor;
import org.example.test1.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishFlavorService extends ServiceImpl<DishFlavorMapper, DishFlavor> implements IDishFlavorService {

    @Override
    public List<DishFlavor> listByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        return list(wrapper);
    }

    @Override
    public void saveBatch(List<DishFlavor> flavors) {
        saveBatch(flavors);
    }

    @Override
    public void deleteByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        remove(wrapper);
    }

    @Override
    public void deleteByDishIds(List<Long> dishIds) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DishFlavor::getDishId, dishIds);
        remove(wrapper);
    }
}
