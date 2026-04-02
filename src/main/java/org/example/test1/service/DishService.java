package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Dish;
import org.example.test1.mapper.DishMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Override
    public void saveDish(Dish dish, Long empId) {
        dish.setCreateUser(empId);
        dish.setUpdateUser(empId);
        save(dish);
    }

    @Override
    public Page<Dish> pageQuery(Integer page, Integer pageSize, String name, Long categoryId) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Dish::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(Dish::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Dish::getUpdateTime);
        page(pageInfo, wrapper);
        return pageInfo;
    }

    @Override
    public void updateDish(Dish dish) {
        updateById(dish);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        removeByIds(ids);
    }

    @Override
    public void updateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            Dish dish = getById(id);
            if (dish == null) {
                throw new BusinessException(ResultCodeEnum.ERROR, "菜品不存在");
            }
            dish.setStatus(status);
            updateById(dish);
        }
    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId, Integer status) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Dish::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Dish::getStatus, status);
        }
        wrapper.orderByAsc(Dish::getSort);
        return list(wrapper);
    }
}
