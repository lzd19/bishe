package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 公告信息表
*/
@Data
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    private String name;
    private String description;
    private Integer employeeId;
    private String employeeName;
}