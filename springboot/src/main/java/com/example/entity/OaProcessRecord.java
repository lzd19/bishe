package com.example.entity;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessRecord {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer process_id;
    private String description;
    private Integer status;
    private Integer operate_user_id;
    private String operate_user;
    private Date create_time;
    private Date update_time;
    private Integer is_deleted;
}
