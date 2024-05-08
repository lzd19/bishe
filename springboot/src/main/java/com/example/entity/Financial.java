package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工
*/
@Data
public class Financial extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private String name;
    private double price;
    private String time;
    private Integer departmentId;
    private String departmentName;
}