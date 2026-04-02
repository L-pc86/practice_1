package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.entity.Setmeal;
import org.example.test1.entity.SetmealDish;
import org.example.test1.service.ISetmealService;
import org.example.test1.service.ISetmealDishService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;

    @Autowired
    private ISetmealDishService setmealDishService;

    @Operation(summary = "新增套餐", description = "添加新套餐")
    @PostMapping
    public Result<String> save(@RequestBody Setmeal setmeal, HttpServletRequest request) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        setmeal.setCreateUser(empId);
        setmeal.setUpdateUser(empId);
        setmealService.save(setmeal);
        return Result.success("新增套餐成功");
    }

    @Operation(summary = "分页查询套餐", description = "分页条件查询套餐列表")
    @GetMapping("/page")
    public Result<Page<Setmeal>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Setmeal::getName, name);
        }
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @Operation(summary = "根据条件查询套餐", description = "条件查询套餐列表")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        wrapper.eq(status != null, Setmeal::getStatus, status);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return Result.success(list);
    }

    @Operation(summary = "修改套餐", description = "修改套餐信息")
    @PutMapping
    public Result<String> update(@RequestBody Setmeal setmeal) {
        setmealService.updateById(setmeal);
        return Result.success("修改套餐成功");
    }

    @Operation(summary = "删除套餐", description = "根据ID删除套餐")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeByIds(ids);
        return Result.success("删除套餐成功");
    }

    @Operation(summary = "修改套餐状态", description = "启售或停售套餐")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return Result.success("状态修改成功");
    }

    @Operation(summary = "根据ID查询套餐", description = "获取套餐详细信息")
    @GetMapping("/{id}")
    public Result<Setmeal> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        return Result.success(setmeal);
    }
}
