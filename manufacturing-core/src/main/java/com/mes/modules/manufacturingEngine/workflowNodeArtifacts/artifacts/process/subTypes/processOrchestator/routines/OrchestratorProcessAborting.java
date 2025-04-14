package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.routines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessAborting;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.services.OrchestratorProcessService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class OrchestratorProcessAborting extends ProcessAborting {
	@Autowired
	private OrchestratorProcessService orchestratorProcessService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		orchestratorProcessService.clearOwnedWorkflowSpecifications(workflowNode.getArtifactId());
		super.execute(workflowNode, event);
	}
}
