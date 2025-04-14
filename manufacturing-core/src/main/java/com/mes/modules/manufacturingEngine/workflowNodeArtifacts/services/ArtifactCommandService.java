package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;


import org.springframework.beans.factory.annotation.Autowired;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.modules.manufacturingEngine.event.services.WorkflowNodeEventService;

public class ArtifactCommandService {
	@Autowired
	protected WorkflowNodeEventService eventService;	

    public WorkflowNodeEventService getEventService() {
		return eventService;
	}

	public void setEventService(WorkflowNodeEventService eventService) {
		this.eventService = eventService;
	}

	public void adquire(String workflowNodeId,EventEntities eventEntity, String owner) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.ADQUIRE);
	}

	public void initiate(String workflowNodeId,EventEntities eventEntity, String owner) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.INITIATE);
	}

	public void cmdStart(String workflowNodeId,EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.START);
	}
	
	public void cmdHold(String workflowNodeId,EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.START);
	}

	public void cmdRestart(String workflowNodeId,EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.RESTART);
	}

	public void cmdAbort(String workflowNodeId,EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.ABORT);
	}

	public void cmdStop(String workflowNodeId,EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.ABORT);
	}

	public void completeStateRoutine(String workflowNodeId, EventEntities eventEntity) {
		eventService.throwEvent(Long.valueOf(workflowNodeId), eventEntity, Commands.COMPLETE);
	}
}
