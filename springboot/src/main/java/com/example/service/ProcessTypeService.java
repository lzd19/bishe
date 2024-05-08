package com.example.service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.entity.*;
import com.example.mapper.OaProcessMapper;
import com.example.mapper.ProcessTypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessTypeService {
    @Resource
    private ProcessTypeMapper processTypeMapper;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private OaProcessService processService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private TaskService taskService;

    @Resource
    private OaProcessMapper processMapper;

    @Resource
    private OaProcessRecordService processRecordService;
    @Resource
    private OaProcessTemplateService processTemplateService;

    public List<ProcessType> findProcessType() {
        //1 查询所有审批分类，返回list集合
        List<ProcessType> processTypeList=processTypeMapper.seleceList();
        //2 遍历返回所有审批分类list集合
        for (ProcessType processType: processTypeList){
            //3 得到每个审批分类，根据审批分类id查询对应审批模板
            //审批分类id
            Integer typeId=processType.getId();
            List<OaProcessTemplate> processTemplateList=processTypeMapper.seleceTemplateList(typeId);

            processType.setProcessTemplateList(processTemplateList);

        }
        return processTypeList;
    }

    public void startUp(ProcessFormVo processFormVo, Integer id) {
        //1.当前用户信息
        Employee employee=employeeService.selectById(id);

        //2 根据审批模板id把模板信息查询
        OaProcessTemplate processTemplate=processTemplateService.selectById(processFormVo.getProcessTemplateId());

        //3 保存提交审批信息到业务表，oa_process
        OaProcess process=new OaProcess();

        //processFormVo复制到process对象里面
        BeanUtils.copyProperties(processFormVo,process);
        //其他值
        process.setStatus(1);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(id);
        process.setFormValue(processFormVo.getFormValues());
        process.setTitle(employee.getName()+"发起"+processTemplate.getName()+"申请");
        processTypeMapper.insert(process);
        Integer id1 = processTypeMapper.getId(process.getProcessCode());
        process.setId(id1);
        //4 启动流程实例 - RuntimeService
        //4.1 流程定义key
        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        //4.2 业务key  processId
        String businessKey = String.valueOf(id1);
        //4.3 流程参数 form表单json数据，转换map集合
        String formValues = processFormVo.getFormValues();
        //formData
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formData = jsonObject.getJSONObject("formData");
        //遍历formData得到内容，封装map集合
        Map<String,Object> map = new HashMap<>();
        for(Map.Entry<String,Object> entry:formData.entrySet()) {
            map.put(entry.getKey(),entry.getValue());
        }
        Map<String,Object> variables = new HashMap<>();
        variables.put("data",map);
        System.out.println(processDefinitionKey);
        System.out.println(businessKey);
        System.out.println(variables);
        //启动流程实例
        ProcessInstance processInstance=runtimeService.startProcessInstanceByKey(processDefinitionKey,
                businessKey,variables);
        //5 查询下一个审批人
        //审批人可能多个
        List<Task> taskList=this.getCurrentTaskList(processInstance.getId());
        System.out.println("==========================");
        System.out.println(processInstance.getId());
        System.out.println("==========================");

        List<String> nameList = new ArrayList<>();
        for (Task task : taskList){
            String assigneeName = task.getAssignee();
            String name=employeeService.getName(assigneeName);
            nameList.add(name);
        }
        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待"+ StringUtils.join(nameList.toArray(), ",")+"审批");
        //7 业务和流程关联  更新oa_process数据
        processTypeMapper.updateById(process);
        //8.记录信息
        processRecordService.record(process.getId(),1,"发起申请",id);
    }
    private List<Task> getCurrentTaskList(String id) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(id).list();
        return taskList;
    }

    public PageInfo<OaProcessVo> findfindPending(Integer page, Integer limit,Integer ID) {
        //1 封装查询条件，根据当前登录的用户名称
        System.out.println(ID);
        Employee employee = employeeService.selectById(ID);
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(employee.getName())
                .orderByTaskCreateTime()
                .desc();
        //2 调用方法分页条件查询，返回list集合，待办任务集合
        //listPage方法有两个参数
        //第一个参数：开始位置  第二个参数：每页显示记录数
        List<Task> taskList=query.listPage(page,limit);
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println(taskList);
        System.out.println("---------------------");
        System.out.println("---------------------");
        Long totalCount= query.count();
        //3 封装返回list集合数据 到 List<OaProcessVo>里面
        //List<Task> -- List<OaProcessVo>
        List<OaProcessVo> processVoList = new ArrayList<>();
        for (Task task:taskList){
            //从task获取流程实例id
            String processInstanceId = task.getProcessInstanceId();
            //根据流程实例id获取实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            //从流程实例对象获取业务key---processId
            String businessKey = processInstance.getBusinessKey();
            if(businessKey == null) {
                continue;
            }
            //根据业务key获取Process对象
            Integer processId = Integer.parseInt(businessKey);
            OaProcess process = processMapper.selectById(processId);
            //Process对象 复制 ProcessVo对象
            OaProcessVo processVo=new OaProcessVo();
            BeanUtils.copyProperties(process,processVo);
            processVo.setTaskId(task.getId());
            //放到最终list集合processVoList
            processVoList.add(processVo);
        }
//        PageInfo<OaProcessVo> pageInfo=new PageInfo<OaProcessVo>();
        return PageInfo.of(processVoList);
    }

    public Map<String, Object> show(Integer id,Integer UserId) {
        //1 根据流程id获取流程信息Process
        OaProcess process=processService.selectById(id);
        //2 根据流程id获取流程记录信息
        List<OaProcessRecord> processRecordList=processRecordService.getProcessRecordById(id);
        //3 根据模板id查询模板信息
        OaProcessTemplate processTemplate=processTemplateService.selectById(process.getProcessTemplateId());
        //4 判断当前用户是否可以审批
        //可以看到信息不一定能审批，不能重复审批
        boolean isApprove = false;
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        for(Task task : taskList){
            //判断任务审批人是否是当前用户
            Employee employee = employeeService.selectById(UserId);
            if (task.getAssignee().equals(employee.getName())){
                isApprove = true;
            }
        }
        //5 查询数据封装到map集合，返回
        Map<String,Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    public void approve(ApprovalVo approvalVo,Integer UserId) {
        //1 从approvalVo获取任务id，根据任务id获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        for(Map.Entry<String,Object> entry:variables.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        //2 判断审批状态值
        if(approvalVo.getStatus() == 1) {
            //2.1 状态值 =1  审批通过
            Map<String, Object> variable = new HashMap<>();
            taskService.complete(taskId,variable);
        } else {
            //2.2 状态值 = -1 驳回，流程直接结束
            this.endTask(taskId);
        }
        //3 记录审批相关过程信息 oa_process_record
        String description = approvalVo.getStatus().intValue() ==1 ? "已通过" : "驳回";
        processRecordService.record(approvalVo.getProcessId(),
                approvalVo.getStatus(),description,UserId);
        //4 查询下一个审批人，更新流程表记录 process表记录
        OaProcess process=processService.selectById(approvalVo.getProcessId());
        //查询任务
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if(!CollectionUtils.isEmpty(taskList)){
            List<String> assignList = new ArrayList<>();
            for(Task task : taskList){
                String assignee = task.getAssignee();
                Employee employee=employeeService.selectByName(assignee);
                assignList.add(employee.getName());
            }
            //更新process流程信息
            process.setDescription("等待" + StringUtils.join(assignList.toArray(), ",") + "审批");
            process.setStatus(1);
        }else {
            if(approvalVo.getStatus().intValue() == 1) {
                process.setDescription("审批完成（通过）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（驳回）");
                process.setStatus(-1);
            }
        }
        processMapper.updateById(process);
    }

    private void endTask(String taskId) {
        //1 根据任务id获取任务对象 Task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //2 获取流程定义模型 BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        //3 获取结束流向节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if(CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode)endEventList.get(0);
        //4 当前流向节点
        FlowNode currentFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //5 清理当前流动方向
        currentFlowNode.getOutgoingFlows().clear();

        //6 创建新流向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        //7 当前节点指向新方向
        List newSequenceFlowList = new ArrayList();
        newSequenceFlowList.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //8 完成当前任务
        taskService.complete(task.getId());
    }

    public PageInfo<OaProcessVo> findProcessed(Integer pageNum, Integer pageSize, Integer userId) {
        Employee employee = employeeService.selectById(userId);
        //封装查询条件
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(employee.getName())
                .finished().orderByTaskCreateTime().desc();
        System.out.println("========================");
        System.out.println(query);
        System.out.println("========================");
        //调用方法条件分页查询，返回list集合
        // 开始位置  和  每页显示记录数
//        PageHelper.startPage(pageNum, pageSize);
        List<HistoricTaskInstance> list = query.listPage(pageNum, pageSize);
        //遍历返回list集合，封装List<ProcessVo>
        List<OaProcessVo> processVoList = new ArrayList<>();
        for(HistoricTaskInstance item : list){
            //流程实例id
            String processInstanceId = item.getProcessInstanceId();
            System.out.println("================================");
            System.out.println(processInstanceId);
            //根据流程实例id查询获取process信息
            OaProcess process=processService.selectByInstanceId(processInstanceId);
            if (process==null) continue;
            System.out.println("================================");
            System.out.println(process);
            System.out.println("================================");
            OaProcessVo processVo1=new OaProcessVo();
            BeanUtils.copyProperties(process,processVo1);
            processVo1.setTaskId("0");
            processVoList.add(processVo1);
        }
        return PageInfo.of(processVoList);
    }

    public PageInfo<ProcessQueryVo> findStarted(Integer id) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(id);
        OaProcessVo processVo=new OaProcessVo();
        List<ProcessQueryVo> list=processMapper.selectPage(processVo,processQueryVo);
        return PageInfo.of(list);
    }
}
