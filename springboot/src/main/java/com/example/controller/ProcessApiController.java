package com.example.controller;

import com.example.common.Result;
import com.example.entity.*;
import com.example.service.OaProcessService;
import com.example.service.OaProcessTemplateService;
import com.example.service.ProcessTypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/processApiController")
public class ProcessApiController {
    @Resource
    private ProcessTypeService processTypeService;

    @Resource
    private OaProcessTemplateService processTemplateService;

    private Integer UserId;

    @GetMapping("/findProcessed")
    public Result findProcessed(
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OaProcessVo> page = processTypeService.findProcessed(pageNum, pageSize,UserId);
        return Result.success(page);
    }

    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processTypeService.approve(approvalVo,UserId);
        return Result.success();
    }
    //查看审批详情信息
    @GetMapping("/show/{id}")
    public Result show(@PathVariable Integer id) {
        Map<String,Object> map = processTypeService.show(id,UserId);
        return Result.success(map);
    }

    @GetMapping("/selectPage/{Userid}")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer limit,
                             @PathVariable Integer Userid){
        UserId=Userid;
        PageInfo<OaProcessVo> pageInfo=processTypeService.findfindPending(page,limit,Userid);
        return Result.success(pageInfo);
    }

    @GetMapping("/findStarted")
    public Result findStarted(){
        PageInfo<ProcessQueryVo> pageInfo=processTypeService.findStarted(UserId);
        return Result.success(pageInfo);
    }

    @PostMapping("/startUp/{Userid}")
    public Result startUp(@RequestBody ProcessFormVo processFormVo,@PathVariable Integer Userid){
        processTypeService.startUp(processFormVo, Userid);
        return Result.success();
    }

    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result get(@PathVariable Integer processTemplateId) {
        OaProcessTemplate processTemplate=processTemplateService.selectById(processTemplateId);
        return Result.success(processTemplate);
    }


    @GetMapping("findProcessType")
    public Result findProcessType() {
        return Result.success(processTypeService.findProcessType());
    }
}
