package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Category;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public interface ICategoryService extends IService<Category> {

    void saveCategory(Category category, Long empId);

    Page<Category> pageQuery(Integer page, Integer pageSize);

    List<Category> listByType(Integer type);

    void updateCategory(Category category);

    void deleteById(Long id);

    void updateStatus(Long id, Integer status);
}
