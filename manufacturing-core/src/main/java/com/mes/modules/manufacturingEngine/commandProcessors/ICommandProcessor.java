package com.mes.modules.manufacturingEngine.commandProcessors;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface ICommandProcessor  {
	public void execute(WorkflowNode workflowNode,ExecutionEvent event);
}
