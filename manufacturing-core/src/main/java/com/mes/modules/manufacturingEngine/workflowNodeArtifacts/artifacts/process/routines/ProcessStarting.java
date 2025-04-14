package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStarting;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ProcessStarting extends WorkflowNodeArtifactRoutineStarting {
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {		
		super.execute(workflowNode, event);
		super.completeRoutine(workflowNode,  event);
	}
}
