package com.boe.esl.model;

import java.io.Serializable;

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
public class Goods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private double price;
	private String address;
	@Column(nullable=false)
	private String unit;
	private String spec;
	private String snid;
	private String scale;
	private String period;
	private String status;
	private String picture;

	@OneToOne
	@JoinColumn(name="labelId")
	private Label label;
}
