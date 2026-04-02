package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Setmeal;
import org.example.test1.mapper.SetmealMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {

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
    public void deleteByIds(List<Long> ids) {
        removeByIds(ids);
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
}
