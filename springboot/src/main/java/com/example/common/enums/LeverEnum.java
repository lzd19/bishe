package com.example.common.enums;

public enum LeverEnum {
    HEADER("部长"),
    EMPLOYEE("员工"),
    ;
    public String lever;

    LeverEnum(String lever) {
        this.lever = lever;
    }
}
