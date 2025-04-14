package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRestarting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class UserActionRestarting extends WorkflowNodeArtifactRoutineRestarting {
	@Override
	public void execute(WorkflowNode workflowNode,ExecutionEvent event) {		
		super.execute(workflowNode, event);		
		this.completeRoutine(workflowNode,event);
	}

	@Override
	public void completeRoutine(WorkflowNode workflowNode,ExecutionEvent event) {	
		super.completeRoutine(workflowNode, event);
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setVariableLocal(workflowNode.getArtifactId(), "routineId", null);
		workflowNodeService.setVariableLocal(workflowNode.getArtifactId(), BaseWorkflowNode.PROP_FAILURE_ID, null);
	}
}
