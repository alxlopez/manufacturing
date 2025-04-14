package com.mes.modules.manufacturingEngine.event.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.EventTypes;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Component
public class ArtifactEventService extends ProcessEngineEventService {
	@Autowired
	private WorkflowNodeService runtimeExecutionService;
	@Autowired
	private WorkflowNodeEventService eventService;

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event) {
		super.validateEvent(event);
		this.validateArtifact(event.getEntityId().toString(), event.getEntityType());
		ExecutionEvent workflowNodeEvent = this.transformArtifactEventToWorkflowNodeEvent(event);
		return eventService.throwEvent(workflowNodeEvent);
	}

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event,EventTypes eventType) {
		super.validateEvent(event);
		this.validateArtifact(event.getEntityId().toString(), event.getEntityType());
		ExecutionEvent workflowNodeEvent = this.transformArtifactEventToWorkflowNodeEvent(event);
		return eventService.throwEvent(workflowNodeEvent,eventType);
	}

	private ExecutionEvent transformArtifactEventToWorkflowNodeEvent(ExecutionEvent event) {
		Artifact artifact = runtimeExecutionService
		.getArtifactObject(ArtifactTypes.valueOf(event.getEntityType().toString()));
		WorkflowNode workflowNode = artifact.getArtifactService().buildWorkflowNodeByArtifactId(event.getEntityId().toString());		
		event.setEntityId(Long.valueOf(workflowNode.getExecutionId()));	
		return event;
	}

	private void validateArtifact(String artifactId, EventEntities eventEntity) {
		Artifact artifact = runtimeExecutionService
		.getArtifactObject(ArtifactTypes.valueOf(eventEntity.toString()));
		artifact.getArtifactService().validateArtifact(artifactId);
	}
}
