package com.boe.esl.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name="tb_board")
public class Board implements BaseModel<Long> {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String code;
	private String name;
	private Timestamp updateTime;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY,mappedBy="board")
	private List<Operator> operators;
}
