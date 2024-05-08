package com.example.mapper;

import com.example.entity.OaProcess;
import com.example.entity.OaProcessTemplate;
import com.example.entity.ProcessType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProcessTypeMapper {
    @Select("select id,name,description from oa_process_type where is_deleted=0")
    List<ProcessType> seleceList();

    @Select("select * from oa_process_template where is_deleted=0 and process_type_id=#{typeId}")
    List<OaProcessTemplate> seleceTemplateList(Integer typeId);

    void insert(OaProcess process);

//    void update(OaProcess process);

    @Select("select id from oa_process where process_code=#{processCode}")
    Integer getId(String processCode);

    void updateById(OaProcess process);
}
