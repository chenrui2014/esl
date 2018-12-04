package com.boe.esl.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractBaseModel<ID extends Serializable> implements BaseModel<ID> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1396905639131014418L;

	@Override
	public ID getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Column(columnDefinition="int default 0")
    private int creator = 0;
	
	@Column(name="create_time")
    private Timestamp createTime;
	
	@Column(columnDefinition="int default 0")
    private int updator = 0;
	
	@Column(name="update_time")
    private Timestamp updateTime;
	
}
