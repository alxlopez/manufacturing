package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines;

import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public abstract class WorkflowNodeArtifactRoutineHolding extends WorkflowNodeArtifactRoutine {	
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setState(workflowNode.getArtifactId(), States.HOLDING);		
	}

	@Override
	public void completeRoutine(WorkflowNode workflowNode, ExecutionEvent event) {
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setState(workflowNode.getArtifactId(), States.HELD);
	}	
}
