package com.mes.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler  {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ManufacturingIllegalArgumentException.class, })
    protected ExceptionResource handleBadRequest(RuntimeException ex) { 
    	ExceptionResource bodyOfResponse = new ExceptionResource("InvalidRequest", ex.getMessage());        
        return  bodyOfResponse ;  		
    }

	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ManufacturingResourceNotFoundException.class })
    protected ExceptionResource handleNotFound(RuntimeException ex) { 
    	ExceptionResource bodyOfResponse = new ExceptionResource("Manufacturing Resource not Found", ex.getMessage());        
        return  bodyOfResponse;
    }

	@ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {ManufacturingConflictException.class })
    protected ExceptionResource handleConflict(RuntimeException ex) { 
        ExceptionResource bodyOfResponse = new ExceptionResource("Manufacturing Conflict", ex.getMessage());        
        return  bodyOfResponse;
    }
}
