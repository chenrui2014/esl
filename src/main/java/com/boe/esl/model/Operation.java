package com.boe.esl.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工序
 * @ClassName Operation
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月28日 下午1:52:00
 * @lastUpdate 2018年8月28日 下午1:52:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tb_operation")
public class Operation implements BaseModel<Long> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	private String content;

}
