package com.boe.esl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="tb_operation_log")
public class OperationLog implements BaseModel<Long> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Timestamp optTime;
    private Integer optType;
    private Boolean isOk;

    @ManyToOne
    @JoinColumn(name="labelId")
    private Label label;
}
