package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Employee;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface IEmployeeService extends IService<Employee> {

    Employee login(String username, String password);

    Employee getByUsername(String username);

    void saveEmployee(Employee employee, Long empId);

    Page<Employee> pageQuery(Integer page, Integer pageSize, String name);

    void updateStatus(Long id, Integer status);

    void updateEmployee(Employee employee);

    void editPassword(Long id, String oldPassword, String newPassword);
}
