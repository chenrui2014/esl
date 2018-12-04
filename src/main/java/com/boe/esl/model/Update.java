package com.boe.esl.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="tb_update")
public class Update implements BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Timestamp updateTime;//更新时间
	private Boolean isOk;
	private String sid;
	private String barCode;
	private String materialName;
	private String materialNum;
	private String customJson;

	@ManyToOne
	@JoinColumn(name="labelId")
	private Label label;
}
