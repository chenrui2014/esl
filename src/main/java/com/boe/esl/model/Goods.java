package com.boe.esl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="tb_goods")
public class Goods implements BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(nullable=false)
	private String name="";//品名
	@Column(nullable=false)
	private double price;//价格
	private String address="";//产地
	@Column(nullable=false)
	private String unit="";//单位
	private String spec="";//规格
	private String snid="";//商品编码
	private String scale="";//等级
	private String period="";//促销时间
	private String status="";
	private String picture="";

	@OneToOne
	@JoinColumn(name="labelId")
	private Label label;
	
//	@ManyToMany(mappedBy="goodsList")
//	private List<Order> orders;
}
