package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.SetmealDish;
import org.example.test1.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SetmealDishService extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {

    @Override
    public List<SetmealDish> listBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealId);
        return list(wrapper);
    }

    @Override
    public void saveBatch(List<SetmealDish> setmealDishes) {
        saveBatch(setmealDishes);
    }

    @Override
    public void deleteBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, setmealId);
        remove(wrapper);
    }

    @Override
    public List<SetmealDish> listBySetmealIds(List<Long> setmealIds) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, setmealIds);
        return list(wrapper);
    }
}
