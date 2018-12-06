package com.boe.esl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ControlMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte deviceType;
    private byte optType;
    private String labelMac;
}
