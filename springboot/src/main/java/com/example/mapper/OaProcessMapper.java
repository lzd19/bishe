package com.example.mapper;

import com.example.entity.OaProcess;
import com.example.entity.OaProcessVo;
import com.example.entity.ProcessQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作员工相关数据接口
*/
public interface OaProcessMapper {

    /**
      * 新增
    */
    int insert(OaProcess oaProcess);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(OaProcess oaProcess);

    /**
      * 根据ID查询
    */
    OaProcess selectById(Integer id);

    /**
      * 查询所有
    */
    List<OaProcess> selectAll(OaProcess oaProcess);

    @Select("select * from oa_process where username = #{username}")
    OaProcess selectByUsername(String username);

    @Select("select * from oa_process where lever='部长' ")
    List<OaProcess> selectHeaders();

    @Select("SELECT * from oa_process where process_instance_id=#{processInstanceId}")
    OaProcess selectByInstanceId(String processInstanceId);

    List<ProcessQueryVo> selectPage(OaProcessVo processVo,@Param("vo") ProcessQueryVo processQueryVo);
}