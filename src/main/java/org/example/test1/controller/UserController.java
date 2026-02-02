package org.example.test1.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public List<User> list() {
        return userService.list();
    }


    //带条件查询接口
    @GetMapping("/listByAge")
    public List<User> listByAge(@RequestParam Integer age) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("age", age);
        return userService.list(wrapper);
    }

    //进阶带条件查询接口LmbdaQueryWrapper
    @GetMapping("/listByAge1")
    public List<User> listByAge1(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(User::getAge, age);
        return userService.list(wrapper);
    }


    //分页查询
    @GetMapping("/page")
    public IPage<User> page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return userService.page(page);
    }


    //分页带age查询
    @GetMapping("/pageByAge")
    public IPage<User> pageByAge(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Integer age) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(User::getAge, age);
        return userService.page(page, wrapper);
    }

    //分页带name查询
    @GetMapping("/pageByName")
    public IPage<User> pageByName(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String name) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, name);
        return userService.page(page, wrapper);
    }


    //删除用户
    @GetMapping("/delete")
    public boolean delete(@RequestParam Integer id) {
        return userService.removeById(id);
    }


}


