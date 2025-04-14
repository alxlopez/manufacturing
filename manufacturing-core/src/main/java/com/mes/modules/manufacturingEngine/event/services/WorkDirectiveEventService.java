package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.EventTypes;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.commandProcessors.workDirective.WorkDirectiveCommandProcessor;

@Component
public class WorkDirectiveEventService  extends ProcessEngineEventService{
	@Autowired
	private WorkDirectiveCommandProcessor commandProcessor;

	public ExecutionEvent processEvent(ExecutionEvent event) {		
		validateEvent(event);	
		event.setEventType(EventTypes.USER);
		event.setReporterId(event.getReporterId());
		commandProcessor.execute(event);
		EventRepository.save(event);
		return event;
	}

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event) {	
		event.setEventType(EventTypes.MANUFACTURING_ENGINE);		
		return processEvent(event);
	}

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event,EventTypes eventType) {	
		event.setEventType(eventType);		
		return processEvent(event);
	}
}
