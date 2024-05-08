package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessVo {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Date createTime;

    private String processCode;

    private Long userId;
    private String name;

    private Long processTemplateId;
    private String processTemplateName;

    private Long processTypeId;
    private String processTypeName;

    private String title;

    private String description;

    private String formProps;

    private String formOptions;

    private String formValues;

    private String processInstanceId;

    private String currentAuditor;

    private Integer status;

    private String taskId;
}
