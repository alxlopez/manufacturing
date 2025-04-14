package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.delegateTask;

import java.util.Date;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;

@Component
public class SetNewScheduledEndTime implements JavaDelegate {
	@Autowired
	private ProcessArtifactService nodeService;
	@Autowired
	private WorkDirectiveRepository workDirectiveRepository;    

	@Override
	public void execute(DelegateExecution execution) {		
		String eventSolvingId = execution.getProcessInstanceId();
		Date newScheduledEndTime = (Date)nodeService.getVariableLocal(eventSolvingId,BaseWorkflowNode.PROP_SCHEDULED_END_TIME);		
		Long workDirectiveId = (Long)nodeService.getVariableLocal(eventSolvingId,"workDirectiveId");
		WorkDirective workDirective = this.workDirectiveRepository.getOne(workDirectiveId);
		String processInstanceFailuredTask =workDirective.getWorkflowSpecificationId();			
		nodeService.setVariableLocal(processInstanceFailuredTask, BaseWorkflowNode.PROP_SCHEDULED_END_TIME, newScheduledEndTime);
	}
}
