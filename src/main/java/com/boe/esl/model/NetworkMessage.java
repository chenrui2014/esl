package com.boe.esl.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NetworkMessage implements Serializable {
    private String gatewayMac;

    private List<String> labelIDList;
}
