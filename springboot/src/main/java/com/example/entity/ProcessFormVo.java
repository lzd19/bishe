package com.example.entity;

import lombok.Data;

@Data
public class ProcessFormVo {
    private Integer processTemplateId;

    private Integer processTypeId;

    private String formValues;
}
