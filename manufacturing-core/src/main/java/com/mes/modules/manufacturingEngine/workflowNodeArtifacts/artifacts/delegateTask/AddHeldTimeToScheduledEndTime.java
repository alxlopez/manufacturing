package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.delegateTask;

import java.util.Date;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class AddHeldTimeToScheduledEndTime implements JavaDelegate {
	@Autowired
	private ArtifactTimeService timeUtility;
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void execute(DelegateExecution execution) {		
		String eventSolvingId = execution.getProcessInstanceId();
		Long eventSolvingEnlapsedTime = (long)runtimeService.getVariableLocal(eventSolvingId,"eventSolvingEnlapsedTime");
		WorkDirective workDirective = (WorkDirective)runtimeService.getVariableLocal(eventSolvingId,"workDirective");
		String processInstanceFailuredTask =workDirective.getWorkflowSpecificationId();		
		String scheduledEndTimeString = (String) runtimeService.getVariableLocal(processInstanceFailuredTask, BaseWorkflowNode.PROP_SCHEDULED_END_TIME);	
		Date scheduledEndTimeDate =timeUtility.getISODate(scheduledEndTimeString);	
		Date newScheduledEndTime = timeUtility.agregateTimeToDate(scheduledEndTimeDate, eventSolvingEnlapsedTime);		
		runtimeService.setVariableLocal(processInstanceFailuredTask, BaseWorkflowNode.PROP_SCHEDULED_END_TIME, newScheduledEndTime);
	}

	public Long calculateEnlapsedTime(String eventSolvingId){		
		String eventSolvingStarTimeString = (String) runtimeService.getVariableLocal(eventSolvingId,
				BaseWorkflowNode.PROP_START_TIME);		
		Date eventSolvingStarTimeDate =timeUtility.getISODate(eventSolvingStarTimeString);		
		Long eventSolvingEnlapsedTime =timeUtility.getEnlapsedTime(eventSolvingStarTimeDate, new Date());		
		return eventSolvingEnlapsedTime;		
	}
}
