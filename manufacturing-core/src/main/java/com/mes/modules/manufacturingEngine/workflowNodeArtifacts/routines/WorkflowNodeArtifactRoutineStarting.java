package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines;

import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkflowNodeArtifactOwners;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public abstract class WorkflowNodeArtifactRoutineStarting extends WorkflowNodeArtifactRoutine {
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setState(workflowNode.getArtifactId(), States.STARTING);
	}

	@Override
	public void completeRoutine(WorkflowNode workflowNode, ExecutionEvent event) {
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setState(workflowNode.getArtifactId(), States.RUNNING);		
	}

	protected void adquire(String workflowNodeId, ArtifactService workflowNodeService, ExecutionEvent event) {
		workflowNodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_OWNER_ID,
				WorkflowNodeArtifactOwners.MANUFACTURING_ENGINE.toString());
	}
}
