package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeUndefinedState extends WorkflowNodeArtifactState {
	@Override
	public void initiate(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getInitiateRoutine().execute(workflowNode, event);
	}
}
