package com.example.entity;

import lombok.Data;

@Data
public class ProcessQueryVo {
    private String keyword;

    private Integer userId;

    private Long processTemplateId;

    private Long processTypeId;

    private String createTimeBegin;
    private String createTimeEnd;

    private Integer status;
}
