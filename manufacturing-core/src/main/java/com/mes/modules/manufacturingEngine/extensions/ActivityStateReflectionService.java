package com.mes.modules.manufacturingEngine.extensions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Component
public class ActivityStateReflectionService {
	@Autowired
	private WorkflowNodeService runtimeExecutionService;

	public void reflect(String parentProcessExecutionId, States activityState) {
		Boolean processActivitiesAtSameState = this.validateProcessChildNodesAtSameState(parentProcessExecutionId, activityState);
		if (activityState.equals(States.RUNNING)) {
			runtimeExecutionService.setState(parentProcessExecutionId, States.RUNNING);
		} else if (activityState.equals(States.HELD) && processActivitiesAtSameState) {
			runtimeExecutionService.setState(parentProcessExecutionId, States.HELD);
		} else if (activityState.equals(States.ABORTED)) {
			if (processActivitiesAtSameState) {
				runtimeExecutionService.setState(parentProcessExecutionId, States.ABORTED);
			} else {
				runtimeExecutionService.setState(parentProcessExecutionId, States.ABORTING);
			}
		} else if (activityState.equals(States.STOPPED) && processActivitiesAtSameState) {
			if (processActivitiesAtSameState) {
				runtimeExecutionService.setState(parentProcessExecutionId, States.STOPPED);
			} else {
				runtimeExecutionService.setState(parentProcessExecutionId, States.STOPPING);
			}
		}
	}

	private Boolean validateProcessChildNodesAtSameState(String parentProcessExecutionId, States state) {
		List<String> childExecutionIds = runtimeExecutionService.findChildExecutionIds(parentProcessExecutionId);
		for (String executionId : childExecutionIds) {
			Artifact artifact = runtimeExecutionService.getArtifact(executionId);
			if (artifact != null) {
				ArtifactService workflowNodeService = artifact.getArtifactService();
				WorkflowNode workflowNodeId = workflowNodeService.buildWorkflowNodeByExecutionId(executionId);
				States executionState = artifact.getArtifactService().getState(workflowNodeId.getArtifactId());
				if ((executionState != state)) {
					return false;
				}
			}
		}
		return true;
	}	
}
