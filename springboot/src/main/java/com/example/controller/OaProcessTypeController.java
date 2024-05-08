package com.example.controller;

import com.example.common.Result;
import com.example.entity.Employee;
import com.example.entity.OaProcessType;
import com.example.service.OaProcessTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 公告信息表前端操作接口
 **/
@RestController
@RequestMapping("/oaProcessType")
public class OaProcessTypeController {

    @Resource
    private OaProcessTypeService oaProcessTypeService;

    @GetMapping("/selectProcessType")
    public Result selectHeaders(){
        List<OaProcessType> list=oaProcessTypeService.selectProcessType();
        return Result.success(list);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody OaProcessType oaProcessType) {
        oaProcessTypeService.add(oaProcessType);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        oaProcessTypeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        oaProcessTypeService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody OaProcessType oaProcessType) {
        oaProcessTypeService.updateById(oaProcessType);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        OaProcessType oaProcessType = oaProcessTypeService.selectById(id);
        return Result.success(oaProcessType);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(OaProcessType oaProcessType ) {
        List<OaProcessType> list = oaProcessTypeService.selectAll(oaProcessType);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(OaProcessType oaProcessType,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OaProcessType> page = oaProcessTypeService.selectPage(oaProcessType, pageNum, pageSize);
        return Result.success(page);
    }

}