package com.boe.esl.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 操作员
 * @ClassName Operator
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月28日 下午1:52:14
 * @lastUpdate 2018年8月28日 下午1:52:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tb_operator")
public class Operator implements BaseModel<Long> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	private String type;
	private Timestamp loginTime;
	@Column(nullable=true,unique=false)
	private short status;
	@OneToOne
	@JoinColumn(name="operationId")
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name="boardId")
	private Board board;
	
}
