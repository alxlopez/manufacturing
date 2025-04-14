package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeAbortingState extends WorkflowNodeArtifactState {
	@Override
	public void complete(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getArtifactService().setVariableLocal(
				workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_STATE,
				States.ABORTED.toString()
		);
		workflowNode.getArtifact().getAbortingRoutine().completeRoutine(workflowNode, event);
	}
}
