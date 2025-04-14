package com.mes.modules.manufacturingEngine.event.services;

import org.flowable.engine.ManagementService;

import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.EventTypes;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.extensions.GetExecutionCmd;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;


@Service
public  class WorkflowNodeEventService extends ProcessEngineEventService {
	@Autowired
	protected EventDefinitionRepository eventDefinitionRepository;
	@Autowired
	protected BeanFactory beanFactory;
	@Autowired
	protected EventRepository EventRepository;
	@Autowired
	protected WorkflowNodeService workflowNodeService;

	private ExecutionEvent processEvent(ExecutionEvent event) {
		validateEvent(event);
		Artifact workflowNodeArtifact = workflowNodeService.getArtifact(String.valueOf(event.getEntityId()));		
		ArtifactService artifactService = workflowNodeArtifact.getArtifactService();
		WorkflowNode workflowNode = artifactService.buildWorkflowNodeByExecutionId(String.valueOf(event.getEntityId()));
		workflowNode.setArtifact(workflowNodeArtifact);
		States state = artifactService.getState(workflowNode.getArtifactId());
		workflowNode.setState(state);
		this.issueArtifactEvent(workflowNode, event);
		this.reportEventAction(workflowNode, event);
		return EventRepository.save(event);
	}

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event){
		event.setEventType(EventTypes.MANUFACTURING_ENGINE);
		return this.processEvent(event);
	}

	@Override
	public ExecutionEvent throwEvent(ExecutionEvent event,EventTypes eventType){
		event.setEventType(eventType);
		return this.processEvent(event);
	}

	public ExecutionEvent throwEvent(Long entityId, EventEntities entityType, Commands command) {
		ExecutionEvent event = buildEvent(entityId, entityType, null, command);
		return this.processEvent(event);
	}

	public ExecutionEvent throwEventByExecutionId(String executionId, Commands command) {
		ExecutionEvent event = buildEvent(Long.valueOf(executionId), EventEntities.WORKFLOW_NODE, null, command);
		return this.processEvent(event);
	}

	public ExecutionEvent throwEventByExecutionId(String executionId, String failureId, Commands command) {
		ExecutionEvent event = buildEvent(Long.valueOf(executionId), EventEntities.WORKFLOW_NODE, null, command);
		EventParameter failureParameter = new EventParameter();
		failureParameter.setCode(BaseWorkflowNode.PROP_FAILURE_ID);
		failureParameter.setValue(failureId);
		event.getParameters().add(failureParameter);
		return this.processEvent(event);
	}

	protected void issueArtifactEvent(WorkflowNode workflowNode, ExecutionEvent event) {
		Artifact artifact = workflowNode.getArtifact();
		artifact.getStateMachine().processEvent(workflowNode, event);
	}

	protected void reportEventAction(WorkflowNode workflowNode, ExecutionEvent event) {
		Commands command = event.getEventDefinition().getCode();
		EventEntities workflowNodeType = event.getEntityType();
		if (command.equals(Commands.COMPLETE)) {
			event.setAction(workflowNodeType + "_" + "COMPLETE");
		} else if (command.equals(Commands.REMOVE)) {
			event.setAction(workflowNodeType + "_" + "REMOVED");
		} else {
			String state = (String) workflowNode.getArtifact().getArtifactService()
					.getVariableLocal(workflowNode.getArtifactId(), BaseWorkflowNode.PROP_STATE);
			String processInstanceId = (String) workflowNode.getArtifact().getArtifactService()
					.getVariableLocal(String.valueOf(workflowNode.getArtifactId()), BaseWorkflowNode.PROP_ROUTINE_ID);
			event.setActionResult(processInstanceId);
			event.setAction(workflowNodeType + "_" + state);
		}
	}

	protected WorkflowNode getWorkflowNode(ExecutionEvent event, Artifact workflowNodeArtifact) {
		WorkflowNode workflowNode = workflowNodeArtifact.getArtifactService()
				.buildWorkflowNodeByExecutionId(String.valueOf(event.getEntityId()));
		return workflowNode;
	}

	protected void validateExecution(String executionId) {
		ManagementService managementService = beanFactory.getBean(ManagementService.class);
		ExecutionEntity execution = (ExecutionEntity) managementService
				.executeCommand(new GetExecutionCmd(executionId));
		if (execution == null) {
			throw new ManufacturingResourceNotFoundException("Execution id " + executionId + " not found");
		}
	}
}
