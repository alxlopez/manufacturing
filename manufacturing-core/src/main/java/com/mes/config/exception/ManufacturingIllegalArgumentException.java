package com.mes.config.exception;

public class ManufacturingIllegalArgumentException extends ManufacturingBaseException {
	private static final long serialVersionUID = -5630317490918508348L;

	public ManufacturingIllegalArgumentException(String message){
		super(message);
	}	

	public ManufacturingIllegalArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
}
