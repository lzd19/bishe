package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessType {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}
