package org.example.test1.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.entity.User;
import org.example.test1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //查询用户列表
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }


    @GetMapping("/listByAge")
    public Result<List<User>> listByAge(@RequestParam Integer age) {
        if (age == null || age < 0) {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("age", age);

        return Result.success(userService.list(wrapper));
    }



    //进阶带条件查询接口LmbdaQueryWrapper
    @GetMapping("/listByAge1")
    public Result<List<User>> listByAge1(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(User::getAge,age);
        return Result.success(userService.list(wrapper));
    }


    //分页查询
    @GetMapping("/page")
    public Result<IPage<User>> page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userService.page(page));
    }

    //分页带age查询
    @GetMapping("/pageByAge")
    public Result<Page<User>> pageByAge(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        Page<User> page = new Page<>(pageNum, pageSize);
        wrapper.eq(User::getAge, age);
        return Result.success(userService.page(page, wrapper));
    }

    //分页带name查询
    @GetMapping("/pageByName")
    public Result<Page<User>> pageByName(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        Page<User> page = new Page<>(pageNum, pageSize);
        wrapper.eq(User::getName, name);
        return Result.success(userService.page(page, wrapper));
    }


    //删除用户
    @GetMapping("/delete")
    public Result<Boolean> delete(@RequestParam Integer id) {
        boolean isDelete = userService.removeById(id);
        return Result.success(isDelete);
    }







}


