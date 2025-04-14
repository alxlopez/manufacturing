package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines;

import java.util.List;

import org.flowable.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ProcessAborting extends WorkflowNodeArtifactRoutineAborting {
	@Autowired
	private ProcessArtifactService processNodeService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		super.execute(workflowNode, event);
		List<Execution> childExecutions = processNodeService
				.findChildExecutionsByParentExecutionId(workflowNode.getArtifactId());
		for (Execution execution : childExecutions) {
			Artifact artifact = processNodeService.getExecutionArtifact(execution.getId());
			if (artifact != null) {									
				eventService.throwEventByExecutionId(execution.getId(), Commands.ABORT);
			}
		}
	}
}
