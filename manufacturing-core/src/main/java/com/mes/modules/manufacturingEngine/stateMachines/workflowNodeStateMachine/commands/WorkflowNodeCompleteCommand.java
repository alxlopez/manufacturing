package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.commands;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states.WorkflowNodeArtifactState;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeCompleteCommand implements WorkflowNodeCommand {
	@Override
	public void execute(WorkflowNodeArtifactState workflowNodeState, WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNodeState.complete(workflowNode, event);		
	}
}
