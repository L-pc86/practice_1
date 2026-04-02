package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Employee;
import org.example.test1.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Override
    public Employee login(String username, String password) {
        Employee employee = getByUsername(username);
        if (employee == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "用户名或密码错误");
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            throw new BusinessException(ResultCodeEnum.ERROR, "用户名或密码错误");
        }
        if (employee.getStatus() == 0) {
            throw new BusinessException(ResultCodeEnum.ERROR, "账号已禁用");
        }
        return employee;
    }

    @Override
    public Employee getByUsername(String username) {
        return lambdaQuery().eq(Employee::getUsername, username).one();
    }

    @Override
    public void saveEmployee(Employee employee, Long empId) {
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        String md5Password = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(md5Password);
        save(employee);
    }

    @Override
    public Page<Employee> pageQuery(Integer page, Integer pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(Employee::getName, name);
        }
        wrapper.orderByDesc(Employee::getUpdateTime);
        page(pageInfo, wrapper);
        return pageInfo;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Employee employee = getById(id);
        if (employee == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "员工不存在");
        }
        employee.setStatus(status);
        updateById(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        updateById(employee);
    }

    @Override
    public void editPassword(Long id, String oldPassword, String newPassword) {
        Employee employee = getById(id);
        if (employee == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "员工不存在");
        }
        String oldMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!oldMd5.equals(employee.getPassword())) {
            throw new BusinessException(ResultCodeEnum.ERROR, "原密码输入错误");
        }
        String newMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        employee.setPassword(newMd5);
        updateById(employee);
    }
}
