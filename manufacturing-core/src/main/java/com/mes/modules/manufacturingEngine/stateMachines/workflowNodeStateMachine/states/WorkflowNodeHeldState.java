package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeHeldState extends WorkflowNodeArtifactState {
	@Override
	public void restart(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getRestartingRoutine()
		.execute(workflowNode, event);
	}

	@Override
	public void abort(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getAbortingRoutine()
		.execute(workflowNode, event);
	}

	@Override
	public void stop(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getStoppingRoutine()
		.execute(workflowNode, event);
	}
}
