package com.mes.dom.event;

import lombok.Data;

@Data
public class EventDirective {
	private Failure failure;
	private String failureComment;
	private String reporterId;	
	private String eventMasterId;
	private String assignee;
	private String candidateGroup;

	public EventDirective() { }
}
