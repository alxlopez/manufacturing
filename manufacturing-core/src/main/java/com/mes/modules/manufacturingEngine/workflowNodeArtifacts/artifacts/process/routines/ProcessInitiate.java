package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineInitiate;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ProcessInitiate extends WorkflowNodeArtifactRoutineInitiate {
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {		
		super.execute(workflowNode, event);
	}
}
