package com.example.mapper;

import com.example.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作员工相关数据接口
*/
public interface EmployeeMapper {

    /**
      * 新增
    */
    int insert(Employee employee);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Employee employee);

    /**
      * 根据ID查询
    */
    Employee selectById(Integer id);

    /**
      * 查询所有
    */
    List<Employee> selectAll(Employee employee);

    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);

    @Select("select * from employee where lever='部长' ")
    List<Employee> selectHeaders();

    @Select("select name from employee where name=#{assigneeName};")
    String getName(String assigneeName);

    @Select("select * from employee where name=#{assignee}")
    Employee selectByName(String assignee);

    @Select("select * from employee where department_id=#{employeeDepartmentId}")
    List<Employee> selectByDepartmentId(Integer employeeDepartmentId);
}