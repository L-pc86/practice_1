package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
            return null;
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            return null;
        }
        if (employee.getStatus() == 0) {
            return null;
        }
        return employee;
    }

    @Override
    public Employee getByUsername(String username) {
        return lambdaQuery().eq(Employee::getUsername, username).one();
    }
}
