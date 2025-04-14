package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.Failure;
import com.mes.modules.manufacturingEngine.event.services.FailureService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactEventSolvingService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class UserActionHolding extends WorkflowNodeArtifactRoutineHolding {
	@Autowired
	private ArtifactEventSolvingService artifactEventSolvingService;
	@Autowired
	private FailureService failureService;
	@Autowired	
	private UserTaskArtifactService artifactService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		Failure failure = null;	
		EventParameter failureIdProperty = event.getParameter(BaseWorkflowNode.PROP_FAILURE_ID);
		if (failureIdProperty != null) {
			failure = failureService.getFailure(Long.valueOf(failureIdProperty.getValue()));
		}
		super.execute(workflowNode, event);
		if (failure != null) {			
			String processInstanceId =this.createEventSolvingProcess(workflowNode,failure.getId(), event);
			artifactService.setFailureId(workflowNode.getArtifactId(), failure.getId());
			artifactService.setVariableLocal(workflowNode.getArtifactId(), BaseWorkflowNode.PROP_ROUTINE_ID, processInstanceId);
		}
		super.completeRoutine(workflowNode, event);
	}

	private String createEventSolvingProcess(WorkflowNode workflowNode,Long failureId, ExecutionEvent event) {
		String comment = null;
		EventParameter eventComment = event.getParameter(BaseWorkflowNode.PROP_COMMENT);
		if (eventComment != null) {
			comment = eventComment.getValue();
		}
		String processInstanceId = artifactEventSolvingService.createEventSolvingProcess(workflowNode.getArtifactId(),
				ArtifactTypes.USER_TASK, failureId, event.getReporterId(), comment);		
		return processInstanceId ;
	}
}
