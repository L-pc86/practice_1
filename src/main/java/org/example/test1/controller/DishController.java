package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.common.utils.JwtUtil;
import org.example.test1.entity.Dish;
import org.example.test1.entity.DishFlavor;
import org.example.test1.service.IDishService;
import org.example.test1.service.IDishFlavorService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
public class DishController {

    @Autowired
    private IDishService dishService;

    @Autowired
    private IDishFlavorService dishFlavorService;

    @Operation(summary = "新增菜品", description = "添加新菜品（含口味）")
    @PostMapping
    public Result<String> save(@RequestBody Dish dish, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long empId = JwtUtil.getEmployeeId(token);
        dishService.saveDish(dish, empId);
        return Result.success("新增菜品成功");
    }

    @GetMapping("/Alllist")
    public Result<List<Dish>> getAlllist(){
        List<Dish> list = dishService.list();
        return Result.success(list);
    }
    

    @Operation(summary = "新增菜品含口味", description = "添加新菜品同时添加口味数据")
    @PostMapping("/withFlavor")
    public Result<String> saveWithFlavor(@RequestBody Dish dish, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long empId = JwtUtil.getEmployeeId(token);
        List<DishFlavor> flavors = dish.getFlavors();
        dishService.saveDishWithFlavor(dish, flavors, empId);
        return Result.success("新增菜品成功");
    }

    @Operation(summary = "分页查询菜品", description = "分页条件查询菜品列表")
    @GetMapping("/page")
    public Result<Page<Dish>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {
        Page<Dish> pageInfo = dishService.pageQuery(page, pageSize, name, categoryId);
        return Result.success(pageInfo);
    }

    @Operation(summary = "根据条件查询菜品", description = "条件查询菜品列表（含口味）")
    @GetMapping("/list")
    public Result<List<Dish>> list(@RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) Integer status) {
        List<Dish> list = dishService.listByCategoryId(categoryId, status);
        for (Dish dish : list) {
            List<DishFlavor> flavors = dishFlavorService.listByDishId(dish.getId());
            dish.setFlavors(flavors);
        }
        return Result.success(list);
    }

    @Operation(summary = "修改菜品", description = "修改菜品信息")
    @PutMapping
    public Result<String> update(@RequestBody Dish dish) {
        dishService.updateDish(dish);
        return Result.success("修改菜品成功");
    }

    @Operation(summary = "修改菜品含口味", description = "修改菜品信息同时更新口味数据")
    @PutMapping("/withFlavor")
    public Result<String> updateWithFlavor(@RequestBody Dish dish) {
        List<DishFlavor> flavors = dish.getFlavors();
        dishService.updateDishWithFlavor(dish, flavors);
        return Result.success("修改菜品成功");
    }

    @Operation(summary = "删除菜品", description = "根据ID删除菜品")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        dishService.deleteWithFlavor(ids);
        return Result.success("删除菜品成功");
    }

    @Operation(summary = "修改菜品状态", description = "启售或停售菜品")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        dishService.updateStatus(ids, status);
        return Result.success("状态修改成功");
    }

    @Operation(summary = "根据ID查询菜品", description = "获取菜品详细信息（含口味）")
    @GetMapping("/{id}")
    public Result<Dish> getById(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        if (dish != null) {
            List<DishFlavor> flavors = dishFlavorService.listByDishId(id);
            dish.setFlavors(flavors);
        }
        return Result.success(dish);
    }
}
