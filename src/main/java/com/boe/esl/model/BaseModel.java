package com.boe.esl.model;

import java.io.Serializable;

public interface BaseModel<ID extends Serializable> extends Serializable {
	ID getId();
}
