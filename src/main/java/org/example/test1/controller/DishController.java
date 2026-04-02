package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.entity.Dish;
import org.example.test1.entity.Category;
import org.example.test1.service.IDishService;
import org.example.test1.service.ICategoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
public class DishController {

    @Autowired
    private IDishService dishService;

    @Autowired
    private ICategoryService categoryService;

    @Operation(summary = "新增菜品", description = "添加新菜品")
    @PostMapping
    public Result<String> save(@RequestBody Dish dish, HttpServletRequest request) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        dish.setCreateUser(empId);
        dish.setUpdateUser(empId);
        dishService.save(dish);
        return Result.success("新增菜品成功");
    }

    @Operation(summary = "分页查询菜品", description = "分页条件查询菜品列表")
    @GetMapping("/page")
    public Result<Page<Dish>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Dish::getName, name);
        }
        wrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @Operation(summary = "根据条件查询菜品", description = "条件查询菜品列表")
    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        wrapper.eq(Dish::getStatus, 1);
        wrapper.orderByAsc(Dish::getSort);
        List<Dish> list = dishService.list(wrapper);
        return Result.success(list);
    }

    @Operation(summary = "修改菜品", description = "修改菜品信息")
    @PutMapping
    public Result<String> update(@RequestBody Dish dish) {
        dishService.updateById(dish);
        return Result.success("修改菜品成功");
    }

    @Operation(summary = "删除菜品", description = "根据ID删除菜品")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        dishService.removeByIds(ids);
        return Result.success("删除菜品成功");
    }

    @Operation(summary = "修改菜品状态", description = "启售或停售菜品")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return Result.success("状态修改成功");
    }

    @Operation(summary = "根据ID查询菜品", description = "获取菜品详细信息")
    @GetMapping("/{id}")
    public Result<Dish> getById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        return Result.success(dish);
    }
}
