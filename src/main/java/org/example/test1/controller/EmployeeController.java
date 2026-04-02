package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.entity.Employee;
import org.example.test1.service.IEmployeeService;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Operation(summary = "员工登录", description = "员工账号登录")
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee loginForm, HttpServletRequest request) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Employee employee = employeeService.login(username, password);
        if (employee == null) {
            return Result.error("登录失败，用户名或密码错误");
        }

        if (employee.getStatus() == 0) {
            return Result.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", employee.getId());
        return Result.success(employee);
    }

    @Operation(summary = "员工登出", description = "退出当前登录")
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @Operation(summary = "新增员工", description = "创建新员工账号")
    @PostMapping
    public Result<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        String md5Password = org.springframework.util.DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(md5Password);

        employeeService.save(employee);
        return Result.success("新增员工成功，默认密码123456");
    }

    @Operation(summary = "分页查询员工", description = "分页条件查询员工列表")
    @GetMapping("/page")
    public Result<Page<Employee>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Employee::getName, name);
        }
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @Operation(summary = "修改员工状态", description = "启用或禁用员工账号")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeService.updateById(employee);
        return Result.success("员工状态修改成功");
    }

    @Operation(summary = "根据ID查询员工", description = "获取员工详细信息")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @Operation(summary = "修改员工信息", description = "更新员工基本信息")
    @PutMapping
    public Result<String> update(@RequestBody Employee employee) {
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    @Operation(summary = "修改密码", description = "修改员工登录密码")
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestParam Long id,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
        Employee employee = employeeService.getById(id);
        String oldMd5 = org.springframework.util.DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldMd5.equals(employee.getPassword())) {
            return Result.error("原密码输入错误");
        }
        String newMd5 = org.springframework.util.DigestUtils.md5DigestAsHex(newPassword.getBytes());
        employee.setPassword(newMd5);
        employeeService.updateById(employee);
        return Result.success("密码修改成功");
    }
}
