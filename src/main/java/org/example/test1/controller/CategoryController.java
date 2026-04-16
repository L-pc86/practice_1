package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.common.utils.JwtUtil;
import org.example.test1.entity.Category;
import org.example.test1.service.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "分类管理", description = "菜品和套餐分类接口")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Operation(summary = "新增分类", description = "新增菜品或套餐分类")
    @PostMapping
    public Result<String> save(@RequestBody Category category, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long empId = JwtUtil.getEmployeeId(token);
        categoryService.saveCategory(category, empId);
        return Result.success("新增分类成功");
    }

    @Operation(summary = "分页查询分类", description = "分页条件查询分类列表")
    @GetMapping("/page")
    public Result<Page<Category>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Category> pageInfo = categoryService.pageQuery(page, pageSize);
        return Result.success(pageInfo);
    }

    @Operation(summary = "根据类型查询分类", description = "查询指定类型的分类列表")
    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(required = false) Integer type) {
        List<Category> list = categoryService.listByType(type);
        return Result.success(list);
    }

    @Operation(summary = "修改分类", description = "修改分类信息")
    @PutMapping
    public Result<String> update(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success("修改分类成功");
    }

    @Operation(summary = "删除分类", description = "根据ID删除分类")
    @DeleteMapping
    public Result<String> delete(@RequestParam Long id) {
        categoryService.deleteById(id);
        return Result.success("删除分类成功");
    }

    @Operation(summary = "修改分类状态", description = "启用或禁用分类")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        categoryService.updateStatus(id, status);
        return Result.success("状态修改成功");
    }
}
