package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工
*/
@Data
public class Salary extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    private Integer employeeId;
    private Integer departmentId;
    private String year;
    private String time;
    private Double price;
    private String note;

    private String employeeName;
    private String departmentName;
}