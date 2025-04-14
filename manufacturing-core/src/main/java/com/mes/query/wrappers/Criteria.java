package com.mes.query.wrappers;

import lombok.Data;

@Data
public class Criteria {
	private String key;
	private String operation;
	private Object value;

	public Criteria() { }

	public Criteria(String key, String operation, Object value) {
		this.key = key;
		this.operation = operation;
		this.value = value;
	}
}
