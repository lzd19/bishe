package com.example.service;

import com.example.entity.Employee;
import com.example.entity.OaProcessRecord;
import com.example.mapper.OaProcessRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OaProcessRecordService {
    @Resource
    private EmployeeService employeeService;

    @Resource
    private OaProcessRecordMapper processRecordMapper;

    public void record(Integer processId, int i, String description, Integer id1) {
        OaProcessRecord processRecord=new OaProcessRecord();
        processRecord.setProcess_id(processId);
        processRecord.setStatus(i);
        processRecord.setDescription(description);
        processRecord.setOperate_user_id(id1);
        Employee employee = employeeService.selectById(id1);
        processRecord.setOperate_user(employee.getName());
        processRecordMapper.insert(processRecord);
    }

    public List<OaProcessRecord> getProcessRecordById(Integer id) {
        return processRecordMapper.getProcessRecordById(id);
    }
}
