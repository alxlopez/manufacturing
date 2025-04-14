package com.mes.modules.workDirective.controller.exceptions;

public interface WorkDirectiveExceptionCodes {
	public String notFound = "workDirective does not exist in repository";
	public String workDirectiveAlreadyInitiated = "workDirective already initiated";
	public String unknownCommand = "unKnown command, allowed commands are: INITIATE";
}
