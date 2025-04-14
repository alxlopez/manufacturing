package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines;

import org.springframework.beans.factory.annotation.Autowired;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.event.services.WorkflowNodeEventService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public abstract class WorkflowNodeArtifactRoutine {
	protected ArtifactService workflowNodeService;
	@Autowired
	protected WorkflowNodeEventService eventService;		
	
	public abstract void execute(WorkflowNode workflowNode, ExecutionEvent event);

	public abstract void completeRoutine(WorkflowNode workflowNode,ExecutionEvent event);
}
