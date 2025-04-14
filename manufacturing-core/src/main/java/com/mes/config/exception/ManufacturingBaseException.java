package com.mes.config.exception;

/**
 * Runtime exception that is the superclass of all Manufacturing exceptions.
*/
public class ManufacturingBaseException extends RuntimeException {
	private static final long serialVersionUID = 6440836837489144017L;

	public ManufacturingBaseException(String message){
        super(message);
    }	

	public ManufacturingBaseException(String message, Throwable cause) {
		super(message, cause);
	  }
}
