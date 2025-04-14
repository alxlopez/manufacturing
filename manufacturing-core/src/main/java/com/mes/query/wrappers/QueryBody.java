package com.mes.query.wrappers;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class QueryBody {
	private List<Link> joins;
	private List<Criteria> filters;
	private String order;
	private String group;

	public QueryBody() {
		this.filters = new ArrayList<Criteria>();
		this.joins = new ArrayList<Link>();
	}
}
