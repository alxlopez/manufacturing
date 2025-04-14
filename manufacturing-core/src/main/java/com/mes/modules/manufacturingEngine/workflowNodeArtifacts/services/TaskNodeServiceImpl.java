package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeCommandService;

@Service("taskNodeService")
public class TaskNodeServiceImpl implements TaskNodeService {	
	@Autowired
    private UserTaskArtifactService userTaskArtifactService ;
	@Autowired
	private WorkflowNodeCommandService  workflowNodeCommandService;

	@Override
	public void cmdRestart(String taskId) {		
		String executionId =userTaskArtifactService.getTaskById(taskId).getExecutionId();
		workflowNodeCommandService.cmdRestart(executionId);		
	}
}
