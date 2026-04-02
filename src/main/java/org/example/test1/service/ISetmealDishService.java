package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.SetmealDish;
import java.util.List;

public interface ISetmealDishService extends IService<SetmealDish> {

    List<SetmealDish> listBySetmealId(Long setmealId);

    void saveBatch(List<SetmealDish> setmealDishes);

    void deleteBySetmealId(Long setmealId);

    List<SetmealDish> listBySetmealIds(List<Long> setmealIds);
}
