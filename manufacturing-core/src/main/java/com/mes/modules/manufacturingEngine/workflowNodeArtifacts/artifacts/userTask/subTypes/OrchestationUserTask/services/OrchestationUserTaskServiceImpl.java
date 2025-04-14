package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.userTask.subTypes.OrchestationUserTask.services;

import java.util.List;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.event.services.WorkflowNodeEventService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Service("orchestationUserTaskService")
public class OrchestationUserTaskServiceImpl implements OrchestrationUserTaskService{
	@Autowired
	private WorkflowNodeService workflowNodeService;
	@Autowired
	private WorkflowNodeEventService workFlowNodeEventService;
	@Autowired
	private UserTaskArtifactService artifactService;

	@Override
	public void issueCommand(WorkflowNode workflowNode, ExecutionEvent event, States stateToValidate, Commands commandToIssue) {
		String processInstanceId = workflowNode.getProcessInstanceId();
		List<ProcessInstance> processInstances = workflowNodeService.getEngineRuntimeService()
				.createProcessInstanceQuery().variableValueEquals(BaseWorkflowNode.PROP_OWNER_ID, processInstanceId)
				.list();
		for (ProcessInstance processInstance : processInstances) {
			Task task = artifactService.getCoreElementService().createTaskQuery()
					.processInstanceId(processInstance.getId()).taskName(workflowNode.getActivityId()).singleResult();
			if (task != null) {
				States state = workflowNode.getArtifact().getArtifactService().getState(task.getId());
				if (state == stateToValidate) {					
					ExecutionEvent clonedEvent =event.clone();
					clonedEvent.setEntityId(Long.valueOf(task.getExecutionId()));
					clonedEvent.setEntityType(EventEntities.WORKFLOW_NODE);					
					workFlowNodeEventService.throwEvent(clonedEvent);
				}
			}
		}
	}
}
