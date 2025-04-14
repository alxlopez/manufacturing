package com.mes.modules.manufacturingEngine.event.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.EventTypes;
import com.mes.dom.event.EventDefinition;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;

public abstract class ProcessEngineEventService {
	@Autowired
	protected EventDefinitionRepository eventDefinitionRepository;
	@Autowired
	protected BeanFactory beanFactory;
	@Autowired
	protected EventRepository EventRepository;

	public ExecutionEvent buildEvent(Long entityId, EventEntities entityType, String reporterId, Commands command) {
		ExecutionEvent event = new ExecutionEvent();
		EventDefinition eventDefinition = new EventDefinition();
		eventDefinition.setCode(command);
		event.setEventDefinition(eventDefinition);
		event.setEntityType(entityType);
		event.setEntityId(entityId);
		event.setReporterId(reporterId);
		return event;
	}

	public ExecutionEvent buildEvent(Long entityId, EventEntities entityType, String reporterId, Commands command,
			Collection<EventParameter> parameters) {
		ExecutionEvent event = this.buildEvent(entityId, entityType, reporterId, command);
		if (parameters != null) {
			event.setParameters(parameters);
		}
		return event;
	}

	public abstract ExecutionEvent throwEvent(ExecutionEvent event);

	public abstract ExecutionEvent throwEvent(ExecutionEvent event, EventTypes eventType);

	protected void validateEvent(ExecutionEvent event) {
		this.reviewRequiredAttributes(event);
		this.setEventTrowTime(event);
		this.setEventDefintionBaseOnCommandCode(event);
	}

	protected void reviewRequiredAttributes(ExecutionEvent event) {
		if (event.getEventDefinition() == null || event.getEntityType() == null || event.getEntityId() == null) {
			throw new ManufacturingIllegalArgumentException(
					"Event could not be created,either eventDefinition, entityType, entityId is required"
			);
		}
	}

	protected void setEventTrowTime(ExecutionEvent event) {
		Date date = new Date();
		event.setThrowTime(date);
	}

	protected void setEventDefintionBaseOnCommandCode(ExecutionEvent event) {
		EventDefinition eventDefinition = event.getEventDefinition();
		eventDefinition = eventDefinitionRepository.findByCode(eventDefinition.getCode());
		event.setEventDefinition(eventDefinition);
	}
}
