package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import com.mes.config.exception.ManufacturingConflictException;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutine;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public abstract class WorkflowNodeArtifactState {
	protected WorkflowNodeArtifactRoutine WorkflowNodeStateRoutine;

	public void initiate(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void remove(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void start(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void hold(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void restart(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void abort(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void stop(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	public void complete(WorkflowNode workflowNode, ExecutionEvent event) {
		throw new ManufacturingConflictException(getCommandExceptionMessage(workflowNode, event));
	}

	protected final String getCommandExceptionMessage(WorkflowNode workflowNode, ExecutionEvent event) {
		String command = event.getEventDefinition().getCode().toString();
		Artifact artifact= workflowNode.getArtifact();
		ArtifactService artifactService = artifact.getArtifactService();
		States state = artifactService.getState(workflowNode.getArtifactId());
		String message = "command " + command + " for " + artifact.getClass().getSimpleName() + " " + workflowNode.getArtifactId() + " with executionId "
				+ workflowNode.getExecutionId() + " is not allowed because its state is " + state.toString();
		return message;
	}
}
