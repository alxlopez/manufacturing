package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.services;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface OrchestrationUserTaskService {
	public void issueCommand(WorkflowNode workflowNode, ExecutionEvent event, States stateToValidate, Commands commandToIssue);
}
