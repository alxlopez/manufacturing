package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.Routines;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class UserActionRunning extends WorkflowNodeArtifactRoutineRunning {
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		// do nothing , all actions are performed by user
	}

	@Override
	public void completeRoutine(WorkflowNode workflowNode, ExecutionEvent event) {
		super.completeRoutine(workflowNode, event);
		UserTaskArtifactService taskNodeService = (UserTaskArtifactService) workflowNode.getArtifact()
				.getArtifactService();
		Map<String, Object> variables = taskNodeService.BuildVariablesMap(event);
		WorkDirective workDirective = (WorkDirective) taskNodeService.getVariable(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_WORK_DIRECTIVE);
		if (workDirective != null) {
			taskNodeService.saveWorkDirectiveParameters(workDirective, variables);
		}				
		taskNodeService.complete(workflowNode.getArtifactId(), variables);
	}
}
