package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class UserActionAborting extends WorkflowNodeArtifactRoutineAborting {
	@Autowired
	private ProcessArtifactService workflowService;	

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		super.execute(workflowNode, event);
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		String deleteReason = null;
		String rutineId = (String) workflowNodeService.getVariableLocal(workflowNode.getArtifactId(), "routineId");
		EventParameter eventComment = event.getParameter("comment");
		if (eventComment != null) {
			deleteReason = eventComment.getValue();
		}
		if (rutineId != null) {
			eventService.throwEvent(Long.valueOf(rutineId),EventEntities.PROCESS, Commands.ABORT);
			workflowService.deleteProcessInstance(rutineId, deleteReason);
		}
		this.completeRoutine(workflowNode, event);
	}

	@Override
	public void completeRoutine(WorkflowNode workflowNode, ExecutionEvent event) {
		super.completeRoutine(workflowNode, event);
		ArtifactService workflowNodeService= workflowNode.getArtifact().getArtifactService();
		workflowNodeService.setVariableLocal(workflowNode.getArtifactId(), "routineId", null);
	}
}
