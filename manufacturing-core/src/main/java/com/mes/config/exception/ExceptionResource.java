package com.mes.config.exception;
import lombok.Data;

@Data
public class ExceptionResource {	
	public String message;
	public String exception;

	public ExceptionResource(String exception, String message) {
		this.exception = exception;
		this.message = message;
	}
}
