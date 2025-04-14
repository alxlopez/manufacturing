package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.config.exception.ManufacturingConflictException;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.Process;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeStoppedState extends WorkflowNodeArtifactState {
	@Autowired
	ProcessArtifactService workflowService;

	@Override
	public void remove(WorkflowNode workflowNode, ExecutionEvent event) {
		if (!Process.class.isAssignableFrom(workflowNode.getArtifact().getClass())){
			throw new ManufacturingConflictException("command REMOVE is only allowed for Processes");
		}
		workflowService.deleteProcessInstance(workflowNode.getArtifactId(), "process Removed");
	}
}
