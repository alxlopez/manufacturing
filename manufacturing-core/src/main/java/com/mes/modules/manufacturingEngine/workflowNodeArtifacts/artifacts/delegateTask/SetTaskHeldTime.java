package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.delegateTask;

import java.util.Date;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.UserTaskArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class SetTaskHeldTime implements JavaDelegate {
	@Autowired
	private ProcessArtifactService runtimeService;
	@Autowired
	private UserTaskArtifactService taskNodeService;
	@Autowired
	private ArtifactTimeService timeUtility;

	@Override
	public void execute(DelegateExecution execution) {
		DelegateExecution processExecution = execution.getParent();	
		String eventSolvingStarTimeString = (String) runtimeService.getVariableLocal(processExecution.getId(),
				BaseWorkflowNode.PROP_START_TIME);		
		Date eventSolvingStarTimeDate =timeUtility.getISODate(eventSolvingStarTimeString);		
		Long eventSolvingEnlapsedTime =timeUtility.getEnlapsedTime(eventSolvingStarTimeDate, new Date());
		String failuredTaskId = (String)runtimeService.getVariableLocal(processExecution.getId(),"failuredTaskId");			
		this.setFailuredTaskExecutionHeldTime(failuredTaskId, eventSolvingEnlapsedTime);
		runtimeService.setVariableLocal(processExecution.getId(), "eventSolvingEnlapsedTime", eventSolvingEnlapsedTime);
	}	

	public void setFailuredTaskExecutionHeldTime(String failuredTaskId,Long HeldDuration){			
		Object heldTimeObject = taskNodeService.getVariableLocal(failuredTaskId, BaseWorkflowNode.PROP_HELD_TIME);		
		Long heldTime = (Long) heldTimeObject;
		if (heldTime==null){
			taskNodeService.setVariableLocal(failuredTaskId, BaseWorkflowNode.PROP_HELD_TIME, HeldDuration);
		} else {
			taskNodeService.setVariableLocal(failuredTaskId, BaseWorkflowNode.PROP_HELD_TIME,heldTime+ HeldDuration);
		}		
	}

	public void setScheduledEndTime(String executionId,String ScheduledEndTime) { }
}
