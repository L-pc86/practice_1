package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Employee;

public interface IEmployeeService extends IService<Employee> {

    Employee login(String username, String password);

    Employee getByUsername(String username);
}
