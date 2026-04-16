package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Setmeal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface ISetmealService extends IService<Setmeal> {

    void saveSetmeal(Setmeal setmeal, Long empId);

    Page<Setmeal> pageQuery(Integer page, Integer pageSize, String name);

    List<Setmeal> listByConditions(Long categoryId, Integer status);

    void updateSetmeal(Setmeal setmeal);

    void deleteByIds(List<Long> ids);

    void updateStatus(List<Long> ids, Integer status);

    Setmeal getSetmealById(Long id);

    void saveSetmealWithDish(Setmeal setmeal, List<org.example.test1.entity.SetmealDish> setmealDishes, Long empId);

    void updateSetmealWithDish(Setmeal setmeal, List<org.example.test1.entity.SetmealDish> setmealDishes);

    void deleteWithDish(List<Long> ids);
}
