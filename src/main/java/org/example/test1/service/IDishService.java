package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface IDishService extends IService<Dish> {

    void saveDish(Dish dish, Long empId);

    Page<Dish> pageQuery(Integer page, Integer pageSize, String name, Long categoryId);

    void updateDish(Dish dish);

    void deleteByIds(List<Long> ids);

    void updateStatus(List<Long> ids, Integer status);

    List<Dish> listByCategoryId(Long categoryId, Integer status);
}
