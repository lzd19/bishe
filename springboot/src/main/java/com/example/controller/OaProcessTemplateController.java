package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.OaProcessTemplate;
import com.example.service.OaProcessTemplateService;
import com.github.pagehelper.PageInfo;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工前端操作接口
 **/
@RestController
@RequestMapping("/oaProcessTemplate")
public class OaProcessTemplateController {

    @Resource
    private OaProcessTemplateService oaProcessTemplateService;

//发布
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Integer id) {
        oaProcessTemplateService.publish(id);

        return Result.success();
    }

    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) throws FileNotFoundException {
        String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        String fileName = file.getOriginalFilename();
        File tempFile = new File(path + "/processes/");// 上传目录
        if (!tempFile.exists()) {// 判断目录是否存在
            tempFile.mkdirs();//创建目录
        }
        String filename=file.getOriginalFilename();// 创建空文件用于写入文件
        File zipFile=new File(path+"/processes/"+filename);
        try { // 保存文件流到本地
            file.transferTo(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(ResultCodeEnum.valueOf("上传失败"));
        }
        Map<String, Object> map = new HashMap<>();
        //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
        map.put("processDefinitionPath", "processes/" + fileName);
        map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
        return Result.success(map);
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody OaProcessTemplate oaProcessTemplate) {
        oaProcessTemplateService.add(oaProcessTemplate);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        oaProcessTemplateService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        oaProcessTemplateService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody OaProcessTemplate oaProcessTemplate) {
        oaProcessTemplateService.updateById(oaProcessTemplate);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        OaProcessTemplate oaProcessTemplate = oaProcessTemplateService.selectById(id);
        return Result.success(oaProcessTemplate);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(OaProcessTemplate oaProcessTemplate ) {
        List<OaProcessTemplate> list = oaProcessTemplateService.selectAll(oaProcessTemplate);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(OaProcessTemplate oaProcessTemplate,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OaProcessTemplate> page = oaProcessTemplateService.selectPage(oaProcessTemplate, pageNum, pageSize);
        return Result.success(page);
    }

}