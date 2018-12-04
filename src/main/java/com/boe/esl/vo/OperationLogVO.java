package com.boe.esl.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OperationLogVO implements BaseVO {
    private Long id;
    private Timestamp optTime;
    private Integer optType;
    private Boolean isOk;
    private Long labelId;
    private String labelCode;
}
