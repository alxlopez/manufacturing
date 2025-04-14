package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.commands;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states.WorkflowNodeArtifactState;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface WorkflowNodeCommand {
	public void execute(WorkflowNodeArtifactState workflowNodeState, WorkflowNode workflowNode, ExecutionEvent event);
}
