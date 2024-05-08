package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Constants;
import com.example.common.enums.LeverEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.OaProcessTemplate;
import com.example.exception.CustomException;
import com.example.mapper.OaProcessTemplateMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员共业务处理
 **/
@Service
public class OaProcessTemplateService {

    @Resource
    private OaProcessTemplateMapper oaProcessTemplateMapper;

    @Resource
    private OaProcessService processService;

    /**
     * 新增
     */
    public void add(OaProcessTemplate oaProcessTemplate) {
        oaProcessTemplateMapper.insert(oaProcessTemplate);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        oaProcessTemplateMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            oaProcessTemplateMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(OaProcessTemplate oaProcessTemplate) {
        oaProcessTemplateMapper.updateById(oaProcessTemplate);
    }

    /**
     * 根据ID查询
     */
    public OaProcessTemplate selectById(Integer id) {
        return oaProcessTemplateMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<OaProcessTemplate> selectAll(OaProcessTemplate oaProcessTemplate) {
        return oaProcessTemplateMapper.selectAll(oaProcessTemplate);
    }

    /**
     * 分页查询
     */
    public PageInfo<OaProcessTemplate> selectPage(OaProcessTemplate oaProcessTemplate, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OaProcessTemplate> list = oaProcessTemplateMapper.selectAll(oaProcessTemplate);
        return PageInfo.of(list);
    }

    public void publish(Integer id) {
        OaProcessTemplate processTemplate=this.selectById(id);
        oaProcessTemplateMapper.updateByStatus(id);
        //优先发布在线流程设计
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())){
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
            System.out.println(processTemplate.getProcessDefinitionPath());
        }
    }
}