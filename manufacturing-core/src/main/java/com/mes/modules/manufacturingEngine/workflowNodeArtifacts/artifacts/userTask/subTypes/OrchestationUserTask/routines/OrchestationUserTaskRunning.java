package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.routines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines.UserActionRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.services.OrchestrationUserTaskService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class OrchestationUserTaskRunning extends UserActionRunning {
	@Autowired
	OrchestrationUserTaskService orchestationUserTaskService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) { }

	@Override
	public void completeRoutine(WorkflowNode workflowNode, ExecutionEvent event) {		
		orchestationUserTaskService.issueCommand(workflowNode, event, States.RUNNING, Commands.COMPLETE);		
		super.completeRoutine(workflowNode, event);
	}
}
