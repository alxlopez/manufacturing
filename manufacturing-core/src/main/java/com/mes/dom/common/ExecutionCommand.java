package com.mes.dom.common;

import com.mes.dom.enumerations.application.Commands;


import lombok.Data;
@Data
public class ExecutionCommand {
	Commands command;	
	String comment;

	public ExecutionCommand() { }
}
