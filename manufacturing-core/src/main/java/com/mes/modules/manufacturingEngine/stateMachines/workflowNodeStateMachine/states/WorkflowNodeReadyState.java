package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeReadyState extends WorkflowNodeArtifactState {
	@Override
	public void start(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getStartingRoutine()
		.execute(workflowNode, event);
	}

	@Override
	public void abort(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getAbortingRoutine()
		.execute(workflowNode, event);
	}
}
