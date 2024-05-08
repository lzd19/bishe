package com.example.entity;

import lombok.Data;

@Data
public class ApprovalVo {
    private Integer processId;

    private String taskId;

    private Integer status;

    private String description;
}
