package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeHoldingState extends WorkflowNodeArtifactState {
	@Override
	public void stop(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getStoppingRoutine().execute(workflowNode, event);
	}

	@Override
	public void abort(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getAbortingRoutine().execute(workflowNode, event);
	}

	@Override
	public void complete(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getHoldingRoutine().completeRoutine(workflowNode, event);
		workflowNode.getArtifact().getArtifactService().setVariableLocal(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_STATE, States.HELD.toString());
	}
}
