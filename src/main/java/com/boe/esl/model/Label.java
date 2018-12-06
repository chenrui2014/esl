package com.boe.esl.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="tb_label")
public class Label implements BaseModel<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String mac;
	private String code;
	private String type;
	private Integer status;
    private String power;

	@ManyToOne
	@JoinColumn(name="gatewayId")
	private Gateway gateway;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, mappedBy="label")
	private List<Update> updates;

	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "label")
	private List<OperationLog> logList;
}
