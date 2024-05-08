package com.example.mapper;

import com.example.entity.OaProcessRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OaProcessRecordMapper {

    void insert(OaProcessRecord processRecord);

    @Select("select * from oa_process_record where process_id=#{id}")
    List<OaProcessRecord> getProcessRecordById(Integer id);
}
