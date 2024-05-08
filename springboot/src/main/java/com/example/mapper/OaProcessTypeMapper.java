package com.example.mapper;

import com.example.entity.Employee;
import com.example.entity.OaProcessType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作oaProcessType相关数据接口
*/
public interface OaProcessTypeMapper {

    /**
      * 新增
    */
    int insert(OaProcessType oaProcessType);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(OaProcessType oaProcessType);

    /**
      * 根据ID查询
    */
    OaProcessType selectById(Integer id);

    /**
      * 查询所有
    */
    List<OaProcessType> selectAll(OaProcessType oaProcessType);

    @Select("select * from oa_process_type")
    List<OaProcessType> selectProcessType();
}