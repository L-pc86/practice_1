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
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
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
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam Integer id) {
        boolean isDelete = userService.removeById(id);
        return Result.success(isDelete);
    }


    //创建用户
    @PostMapping("/create")
    public Result<Boolean> create(@Valid @RequestBody User user) {
        boolean isSave = userService.save(user);
        return Result.success(isSave);
    }

    //更新用户
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody User user) {
        boolean isUpdate = userService.updateById(user);
        return Result.success(isUpdate);
    }

    //根据ID查询用户
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    //根据ID查询用户（带条件）
    @GetMapping("/getById")
    public Result<User> getUserById(@RequestParam Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    //统计用户总数
    @GetMapping("/count")
    public Result<Long> count() {
        Long total = userService.count();
        return Result.success(total);
    }

    //根据条件统计用户数量
    @GetMapping("/countByAge")
    public Result<Long> countByAge(@RequestParam Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAge, age);
        Long total = userService.count(wrapper);
        return Result.success(total);
    }

    //批量删除用户
    @DeleteMapping("/batch")
    public Result<Boolean> deleteBatch(@RequestBody List<Integer> ids) {
        boolean isDelete = userService.removeByIds(ids);
        return Result.success(isDelete);
    }

    //根据ID数组查询用户列表
    @GetMapping("/listByIds")
    public Result<List<User>> listByIds(@RequestParam List<Integer> ids) {
        List<User> list = userService.listByIds(ids);
        return Result.success(list);
    }

    //根据name模糊查询
    @GetMapping("/listByNameLike")
    public Result<List<User>> listByNameLike(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, name);
        return Result.success(userService.list(wrapper));
    }

    //多条件组合查询（name精确 + age范围）
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

    //分页 + 多条件组合查询
    @GetMapping("/pageSearch")
    public Result<Page<User>> pageSearch(@RequestParam Integer pageNum,
                                          @RequestParam Integer pageSize,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(User::getName, name);
        }
        if (age != null) {
            wrapper.eq(User::getAge, age);
        }
        wrapper.orderByDesc(User::getCreateTime);
        Page<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userService.page(page, wrapper));
    }

    //根据邮箱模糊查询
    @GetMapping("/listByEmailLike")
    public Result<List<User>> listByEmailLike(@RequestParam String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getEmail, email);
        return Result.success(userService.list(wrapper));
    }

    //根据name精确查询
    @GetMapping("/listByName")
    public Result<List<User>> listByName(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        return Result.success(userService.list(wrapper));
    }

    //根据name删除
    @DeleteMapping("/deleteByName")
    public Result<Boolean> deleteByName(@RequestParam String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, name);
        boolean isDelete = userService.remove(wrapper);
        return Result.success(isDelete);
    }

    //年龄范围查询
    @GetMapping("/listByAgeRange")
    public Result<List<User>> listByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(User::getAge, minAge, maxAge);
        return Result.success(userService.list(wrapper));
    }

    //批量新增用户
    @PostMapping("/batchSave")
    public Result<Boolean> batchSave(@RequestBody List<User> users) {
        boolean isSave = userService.saveBatch(users);
        return Result.success(isSave);
    }

    //根据ID更新name
    @PutMapping("/updateName")
    public Result<Boolean> updateName(@RequestParam Integer id, @RequestParam String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        boolean isUpdate = userService.updateById(user);
        return Result.success(isUpdate);
    }


}


