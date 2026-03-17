package org.example.test1.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.test1.common.Result;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.entity.User;
import org.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "查询用户列表", description = "获取所有用户列表")
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }


    @Operation(summary = "根据年龄查询用户", description = "精确匹配年龄")
    @GetMapping("/listByAge")
    public Result<List<User>> listByAge(@RequestParam Integer age) {
        if (age == null || age < 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("age", age);

        return Result.success(userService.list(wrapper));
    }


    @Operation(summary = "根据年龄范围查询", description = "查询大于等于指定年龄的用户")
    @GetMapping("/listByAge1")
    public Result<List<User>> listByAge1(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(User::getAge,age);
        return Result.success(userService.list(wrapper));
    }


    @Operation(summary = "分页查询", description = "分页查询所有用户")
    @GetMapping("/page")
    public Result<IPage<User>> page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userService.page(page));
    }

    @Operation(summary = "分页+年龄查询", description = "分页查询指定年龄的用户")
    @GetMapping("/pageByAge")
    public Result<Page<User>> pageByAge(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        Page<User> page = new Page<>(pageNum, pageSize);
        wrapper.eq(User::getAge, age);
        return Result.success(userService.page(page, wrapper));
    }

    @Operation(summary = "分页+姓名查询", description = "分页查询指定姓名的用户")
    @GetMapping("/pageByName")
    public Result<Page<User>> pageByName(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        Page<User> page = new Page<>(pageNum, pageSize);
        wrapper.eq(User::getName, name);
        return Result.success(userService.page(page, wrapper));
    }


    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam Integer id) {
        boolean isDelete = userService.removeById(id);
        return Result.success(isDelete);
    }


    @Operation(summary = "创建用户", description = "新增一个用户")
    @PostMapping("/create")
    public Result<Boolean> create(@Valid @RequestBody User user) {
        boolean isSave = userService.save(user);
        return Result.success(isSave);
    }

    @Operation(summary = "更新用户", description = "根据ID更新用户信息")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody User user) {
        boolean isUpdate = userService.updateById(user);
        return Result.success(isUpdate);
    }

    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户信息")
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户信息（请求参数）")
    @GetMapping("/getById")
    public Result<User> getUserById(@RequestParam Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @Operation(summary = "统计用户总数", description = "统计所有用户数量")
    @GetMapping("/count")
    public Result<Long> count() {
        Long total = userService.count();
        return Result.success(total);
    }

    @Operation(summary = "按年龄统计用户", description = "统计指定年龄的用户数量")
    @GetMapping("/countByAge")
    public Result<Long> countByAge(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAge, age);
        Long total = userService.count(wrapper);
        return Result.success(total);
    }

    @Operation(summary = "批量删除用户", description = "根据ID数组批量删除用户")
    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@RequestBody List<Integer> ids) {
        boolean isDelete = userService.removeByIds(ids);
        return Result.success(isDelete);
    }

    @Operation(summary = "根据ID列表查询", description = "根据多个ID查询用户列表")
    @GetMapping("/listByIds")
    public Result<List<User>> listByIds(@RequestParam List<Integer> ids) {
        List<User> list = userService.listByIds(ids);
        return Result.success(list);
    }

    @Operation(summary = "根据姓名模糊查询", description = "根据姓名模糊匹配查询用户")
    @GetMapping("/listByNameLike")
    public Result<List<User>> listByNameLike(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, name);
        return Result.success(userService.list(wrapper));
    }

    @Operation(summary = "多条件组合查询", description = "支持name、age、年龄范围多条件查询")
    @GetMapping("/search")
    public Result<List<User>> search(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) Integer age,
                                      @RequestParam(required = false) Integer minAge,
                                      @RequestParam(required = false) Integer maxAge) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.eq(User::getName, name);
        }
        if (age != null) {
            wrapper.eq(User::getAge, age);
        }
        if (minAge != null) {
            wrapper.ge(User::getAge, minAge);
        }
        if (maxAge != null) {
            wrapper.le(User::getAge, maxAge);
        }
        return Result.success(userService.list(wrapper));
    }

    @Operation(summary = "根据邮箱模糊查询", description = "根据邮箱模糊匹配查询用户")
    @GetMapping("/listByEmailLike")
    public Result<List<User>> listByEmailLike(@RequestParam String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getEmail, email);
        return Result.success(userService.list(wrapper));
    }

    @Operation(summary = "根据姓名精确查询", description = "根据姓名精确匹配查询用户")
    @GetMapping("/listByName")
    public Result<List<User>> listByName(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        return Result.success(userService.list(wrapper));
    }

    @Operation(summary = "根据姓名删除", description = "根据姓名删除用户")
    @DeleteMapping("/deleteByName")
    public Result<Boolean> deleteByName(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        boolean isDelete = userService.remove(wrapper);
        return Result.success(isDelete);
    }

    @Operation(summary = "年龄范围查询", description = "查询指定年龄范围内的用户")
    @GetMapping("/listByAgeRange")
    public Result<List<User>> listByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(User::getAge, minAge, maxAge);
        return Result.success(userService.list(wrapper));
    }

    @Operation(summary = "批量新增用户", description = "批量插入多个用户")
    @PostMapping("/batchSave")
    public Result<Boolean> batchSave(@RequestBody List<User> users) {
        boolean isSave = userService.saveBatch(users);
        return Result.success(isSave);
    }

    @Operation(summary = "更新用户姓名", description = "根据ID更新用户姓名")
    @PutMapping("/updateName")
    public Result<Boolean> updateName(@RequestParam Integer id, @RequestParam String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        boolean isUpdate = userService.updateById(user);
        return Result.success(isUpdate);
    }


}


