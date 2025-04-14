package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines;

import java.util.List;

import org.flowable.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineHolding;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ProcessHolding extends WorkflowNodeArtifactRoutineHolding {
	@Autowired
	private ProcessArtifactService processNodeService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		super.execute(workflowNode, event);
		List<Execution> childExecutions = processNodeService.findChildExecutionsByParentExecutionId(workflowNode.getArtifactId());
		for (Execution execution : childExecutions) {			
			Artifact artifact = processNodeService.getExecutionArtifact(execution.getId());			
			if (artifact != null) {
				ArtifactService workflowNodeService = artifact.getArtifactService();
				WorkflowNode childWorkflowNode = workflowNodeService.buildWorkflowNodeByExecutionId(execution.getId());
				States currentState = workflowNodeService.getState(childWorkflowNode.getArtifactId());
				if (currentState == States.RUNNING) {
					eventService.throwEventByExecutionId(execution.getId(), Commands.HOLD);
				}
			}
		}
	}
}