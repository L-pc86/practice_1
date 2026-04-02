package org.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.test1.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
