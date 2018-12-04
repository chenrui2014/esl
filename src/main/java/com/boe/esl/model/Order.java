package com.boe.esl.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 * @ClassName Order
 * @Description TODO()
 * @author chen
 * @version v1.0
 * @create 2018年8月28日 下午1:52:28
 * @lastUpdate 2018年8月28日 下午1:52:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tb_order")
public class Order implements BaseModel<Long> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private int count;
	private Timestamp orderTime;
	/**
	 * 订单状态，0：未下发，1：已下发
	 */
	private String status;
	
	@OneToOne
	@JoinColumn(name="goodsId")
	private Goods goods;
	
//	@ManyToMany
//    @JoinTable(name = "order_goods",joinColumns = @JoinColumn(name = "order_id"),
//    inverseJoinColumns = @JoinColumn(name = "goods_id"))
//	private List<Goods> goodsList;
}
