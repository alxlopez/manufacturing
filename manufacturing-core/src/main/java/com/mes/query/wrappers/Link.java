package com.mes.query.wrappers;

import lombok.Data;

@Data
public class Link {
	private String parameter;
	private String entity;

	public Link() { }

	public Link(String parameter, String entity) {
		this.parameter = parameter;
		this.entity = entity;
	}
}
