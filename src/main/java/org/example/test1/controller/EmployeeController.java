package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.common.utils.JwtUtil;
import org.example.test1.entity.Employee;
import org.example.test1.service.IEmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employee")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Operation(summary = "员工登录", description = "员工账号登录，返回JWT Token")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Employee loginForm) {
        Employee employee = employeeService.login(loginForm.getUsername(), loginForm.getPassword());
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", employee.getId());
        String token = JwtUtil.createToken(claims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("employee", employee);
        return Result.success(data);
    }

    @Operation(summary = "员工登出", description = "退出当前登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }

    @Operation(summary = "新增员工", description = "创建新员工账号")
    @PostMapping
    public Result<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long empId = JwtUtil.getEmployeeId(token);
        employeeService.saveEmployee(employee, empId);
        return Result.success("新增员工成功，默认密码123456");
    }

    @Operation(summary = "分页查询员工", description = "分页条件查询员工列表")
    @GetMapping("/page")
    public Result<Page<Employee>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        Page<Employee> pageInfo = employeeService.pageQuery(page, pageSize, name);
        return Result.success(pageInfo);
    }

    @Operation(summary = "修改员工状态", description = "启用或禁用员工账号")
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        employeeService.updateStatus(id, status);
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
        employeeService.updateEmployee(employee);
        return Result.success("员工信息修改成功");
    }

    @Operation(summary = "修改密码", description = "修改员工登录密码")
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestParam Long id,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
        employeeService.editPassword(id, oldPassword, newPassword);
        return Result.success("密码修改成功");
    }
}
