package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.entity.Setmeal;
import org.example.test1.service.ISetmealService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
public class SetmealController {

    @Autowired
    private ISetmealService setmealService;

    @Operation(summary = "新增套餐", description = "添加新套餐")
    @PostMapping
    public Result<String> save(@RequestBody Setmeal setmeal, HttpServletRequest request) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        setmealService.saveSetmeal(setmeal, empId);
        return Result.success("新增套餐成功");
    }

    @Operation(summary = "分页查询套餐", description = "分页条件查询套餐列表")
    @GetMapping("/page")
    public Result<Page<Setmeal>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Setmeal> pageInfo = setmealService.pageQuery(page, pageSize, name);
        return Result.success(pageInfo);
    }

    @Operation(summary = "根据条件查询套餐", description = "条件查询套餐列表")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) Integer status) {
        List<Setmeal> list = setmealService.listByConditions(categoryId, status);
        return Result.success(list);
    }

    @Operation(summary = "修改套餐", description = "修改套餐信息")
    @PutMapping
    public Result<String> update(@RequestBody Setmeal setmeal) {
        setmealService.updateSetmeal(setmeal);
        return Result.success("修改套餐成功");
    }

    @Operation(summary = "删除套餐", description = "根据ID删除套餐")
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success("删除套餐成功");
    }

    @Operation(summary = "修改套餐状态", description = "启售或停售套餐")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam List<Long> ids, @RequestParam Integer status) {
        setmealService.updateStatus(ids, status);
        return Result.success("状态修改成功");
    }

    @Operation(summary = "根据ID查询套餐", description = "获取套餐详细信息")
    @GetMapping("/{id}")
    public Result<Setmeal> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getSetmealById(id);
        return Result.success(setmeal);
    }
}
