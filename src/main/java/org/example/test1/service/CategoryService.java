package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Category;
import org.example.test1.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

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
