package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessTemplate {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;

    private Long processTypeId;

    private String formProps;

    private String formOptions;

    private String description;

    private String processDefinitionKey;

    private String processDefinitionPath;

    private String processModelId;

    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;

    private String processName;
}
