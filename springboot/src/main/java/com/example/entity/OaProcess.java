package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcess {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String processCode;
    private Integer userId;
    private Integer processTemplateId;
    private Integer processTypeId;
    private String title;
    private String description;
    private String formValue;
    private String processInstanceId;
    private String currentAuditor;
    private int status;
    private Date createTime;
    private Date updateTime;
    private int isDelete;

    private String userName;
    private String processTemplateName;
    private String processTypeName;
}
