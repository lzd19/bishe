package com.example.mapper;

import com.example.entity.OaProcessTemplate;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作员工相关数据接口
*/
public interface OaProcessTemplateMapper {

    /**
      * 新增
    */
    int insert(OaProcessTemplate oaProcessTemplate);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(OaProcessTemplate oaProcessTemplate);

    /**
      * 根据ID查询
    */
    OaProcessTemplate selectById(Integer id);

    /**
      * 查询所有
    */
    List<OaProcessTemplate> selectAll(OaProcessTemplate oaProcessTemplate);

    @Update("update oa_process_template set status=1 where id=#{id}")
    void updateByStatus(Integer id);

    @Select("select * from oaProcessTemplate where name = #{name}")
    OaProcessTemplate selectByUsername(String username);

    @Select("select * from oaProcessTemplate where lever='部长' ")
    List<OaProcessTemplate> selectHeaders();
}