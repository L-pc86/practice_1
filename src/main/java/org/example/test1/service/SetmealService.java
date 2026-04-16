package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Setmeal;
import org.example.test1.entity.SetmealDish;
import org.example.test1.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

    @Autowired
    private ISetmealDishService setmealDishService;

    @Override
    public void saveSetmeal(Setmeal setmeal, Long empId) {
        setmeal.setCreateUser(empId);
        setmeal.setUpdateUser(empId);
        save(setmeal);
    }

    @Override
    public Page<Setmeal> pageQuery(Integer page, Integer pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Setmeal::getName, name);
        }
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        page(pageInfo, wrapper);
        return pageInfo;
    }

    @Override
    public List<Setmeal> listByConditions(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        wrapper.eq(status != null, Setmeal::getStatus, status);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        return list(wrapper);
    }

    @Override
    public void updateSetmeal(Setmeal setmeal) {
        updateById(setmeal);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = getById(id);
            if (setmeal != null && setmeal.getStatus() == 1) {
                throw new BusinessException(ResultCodeEnum.ERROR, "起售中的套餐不能删除");
            }
        }
        removeByIds(ids);
        for (Long id : ids) {
            setmealDishService.deleteBySetmealId(id);
        }
    }

    @Override
    public void updateStatus(List<Long> ids, Integer status) {
        for (Long id : ids) {
            Setmeal setmeal = getById(id);
            if (setmeal == null) {
                throw new BusinessException(ResultCodeEnum.ERROR, "套餐不存在");
            }
            setmeal.setStatus(status);
            updateById(setmeal);
        }
    }

    @Override
    public Setmeal getSetmealById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void saveSetmealWithDish(Setmeal setmeal, List<SetmealDish> setmealDishes, Long empId) {
        setmeal.setCreateUser(empId);
        setmeal.setUpdateUser(empId);
        save(setmeal);

        for (SetmealDish dish : setmealDishes) {
            dish.setSetmealId(setmeal.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void updateSetmealWithDish(Setmeal setmeal, List<SetmealDish> setmealDishes) {
        updateById(setmeal);

        setmealDishService.deleteBySetmealId(setmeal.getId());

        for (SetmealDish dish : setmealDishes) {
            dish.setSetmealId(setmeal.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = getById(id);
            if (setmeal != null && setmeal.getStatus() == 1) {
                throw new BusinessException(ResultCodeEnum.ERROR, "起售中的套餐不能删除");
            }
        }
        removeByIds(ids);
        for (Long id : ids) {
            setmealDishService.deleteBySetmealId(id);
        }
    }
}
