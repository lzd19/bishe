package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.LeverEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.OaProcess;
import com.example.exception.CustomException;
import com.example.mapper.OaProcessMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 员共业务处理
 **/
@Service
public class OaProcessService {

    @Resource
    private OaProcessMapper oaProcessMapper;

    @Resource
    private RepositoryService repositoryService;


    public void deployByZip(String deployPath) {
        // 定义zip输入流
        InputStream inputStream =
                this.getClass().getClassLoader().getResourceAsStream(deployPath);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 新增
     */
    public void add(OaProcess oaProcess) {
        oaProcessMapper.insert(oaProcess);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        oaProcessMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            oaProcessMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(OaProcess oaProcess) {
        oaProcessMapper.updateById(oaProcess);
    }

    /**
     * 根据ID查询
     */
    public OaProcess selectById(Integer id) {
        return oaProcessMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<OaProcess> selectAll(OaProcess oaProcess) {
        return oaProcessMapper.selectAll(oaProcess);
    }

    /**
     * 分页查询
     */
    public PageInfo<OaProcess> selectPage(OaProcess oaProcess, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OaProcess> list = oaProcessMapper.selectAll(oaProcess);
        return PageInfo.of(list);
    }

    public OaProcess selectByInstanceId(String processInstanceId) {
        return oaProcessMapper.selectByInstanceId(processInstanceId);
    }
}