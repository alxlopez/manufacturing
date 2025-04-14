package com.mes.config.exception;

public class ManufacturingConflictException extends ManufacturingBaseException {
	private static final long serialVersionUID = -771178043011565921L;

	public ManufacturingConflictException(String message){
        super(message);
    }	

	public ManufacturingConflictException(String message, Throwable cause) {
		super(message, cause);
	}
}
