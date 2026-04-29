package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Category;
import org.example.test1.entity.Dish;
import org.example.test1.entity.Setmeal;
import org.example.test1.mapper.CategoryMapper;
import org.example.test1.mapper.DishMapper;
import org.example.test1.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void saveCategory(Category category, Long empId) {
        category.setCreateUser(empId);
        category.setUpdateUser(empId);
        save(category);
    }

    @Override
    public Page<Category> pageQuery(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Category::getSort);
        page(pageInfo, wrapper);
        return pageInfo;
    }

    @Override
    public List<Category> listByType(Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(Category::getType, type);
        }
        wrapper.orderByAsc(Category::getSort);
        return list(wrapper);
    }

    @Override
    public void updateCategory(Category category) {
        updateById(category);
    }

    @Override
    public void deleteById(Long id) {
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(Dish::getCategoryId, id);
        long dishCount = dishMapper.selectCount(dishWrapper);
        if (dishCount > 0) {
            throw new BusinessException(ResultCodeEnum.ERROR, "当前分类下存在菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.eq(Setmeal::getCategoryId, id);
        long setmealCount = setmealMapper.selectCount(setmealWrapper);
        if (setmealCount > 0) {
            throw new BusinessException(ResultCodeEnum.ERROR, "当前分类下存在套餐，无法删除");
        }

        removeById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "分类不存在");
        }
        category.setStatus(status);
        updateById(category);
    }
}
