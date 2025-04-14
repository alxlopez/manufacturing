package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface WorkflowNodeStateMachine {
	public void processEvent(WorkflowNode workflowNode, ExecutionEvent event);	
}
