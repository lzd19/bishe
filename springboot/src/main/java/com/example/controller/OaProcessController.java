package com.example.controller;

import com.example.common.Result;
import com.example.entity.OaProcess;
import com.example.service.OaProcessService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工前端操作接口
 **/
@RestController
@RequestMapping("/oaProcess")
public class OaProcessController {

    @Resource
    private OaProcessService oaProcessService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody OaProcess oaProcess) {
        oaProcessService.add(oaProcess);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        oaProcessService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        oaProcessService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody OaProcess oaProcess) {
        oaProcessService.updateById(oaProcess);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        OaProcess oaProcess = oaProcessService.selectById(id);
        return Result.success(oaProcess);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(OaProcess oaProcess ) {
        List<OaProcess> list = oaProcessService.selectAll(oaProcess);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(OaProcess oaProcess,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OaProcess> page = oaProcessService.selectPage(oaProcess, pageNum, pageSize);
        return Result.success(page);
    }

}