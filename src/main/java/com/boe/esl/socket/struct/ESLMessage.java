package com.boe.esl.socket.struct;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESLMessage implements Serializable {

	/**
	 * @field serialVersionUID:TODO()
	 */
	private static final long serialVersionUID = 1L;
	private ESLHeader eslHeader;
	private byte[] content;
}
