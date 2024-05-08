package com.example.service;

import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Employee;
import com.example.entity.OaProcessType;
import com.example.entity.ProcessType;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.OaProcessTypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表业务处理
 **/
@Service
public class OaProcessTypeService {

    @Resource
    private OaProcessTypeMapper oaProcessTypeMapper;
    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private OaProcessTemplateService processTemplateService;
    /**
     * 新增
     */
    public void add(OaProcessType oaProcessType) {
        oaProcessTypeMapper.insert(oaProcessType);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        oaProcessTypeMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            oaProcessTypeMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(OaProcessType oaProcessType) {
        oaProcessTypeMapper.updateById(oaProcessType);
    }

    /**
     * 根据ID查询
     */
    public OaProcessType selectById(Integer id) {
        return oaProcessTypeMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<OaProcessType> selectAll(OaProcessType oaProcessType) {
        return oaProcessTypeMapper.selectAll(oaProcessType);
    }

    /**
     * 分页查询
     */
    public PageInfo<OaProcessType> selectPage(OaProcessType oaProcessType, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OaProcessType> list = oaProcessTypeMapper.selectAll(oaProcessType);
        return PageInfo.of(list);
    }

    public List<OaProcessType> selectProcessType() {
        return oaProcessTypeMapper.selectProcessType();
    }

}