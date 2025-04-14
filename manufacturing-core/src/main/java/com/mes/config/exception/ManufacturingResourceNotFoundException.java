package com.mes.config.exception;

public class ManufacturingResourceNotFoundException extends ManufacturingBaseException {
	private static final long serialVersionUID = 7807358746768502005L;

	public ManufacturingResourceNotFoundException(String message){
		super(message);
	}	

	public ManufacturingResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
