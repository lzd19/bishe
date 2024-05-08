package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProcessType {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String description;

    private List<OaProcessTemplate> processTemplateList;
}
