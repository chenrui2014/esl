package com.boe.esl.vo;

import lombok.Data;

@Data
public class UpdateVO implements BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long labelId;
	private String labelCode;
	private String updateTime;
	private String sid;
	private String barCode;
	private String materialName;
	private Short materialNum;
	private String customJson;
	private Boolean isOk;
}
