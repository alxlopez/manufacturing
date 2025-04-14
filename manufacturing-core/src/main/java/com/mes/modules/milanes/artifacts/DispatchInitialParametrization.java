package com.mes.modules.milanes.artifacts;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;

@Component
public class DispatchInitialParametrization implements JavaDelegate{
	@Autowired
	private ProcessArtifactService processNodeService;
	
	@Override
	public void execute(DelegateExecution execution) {
		String processInstanceId = execution.getProcessInstanceId();
		WorkDirective workDirective = (WorkDirective) processNodeService.getVariableLocal(processInstanceId,"workDirective");
		String receiverId = workDirective.getParameter("receiverId").getValue();
		String receiverType = workDirective.getParameter("receiverType").getValue();
		String receiverName = workDirective.getParameter("receiverName").getValue();
		String receiverCity = workDirective.getParameter("receiverCity").getValue();
		processNodeService.setVariableLocal(processInstanceId, "receiverId", receiverId);
		processNodeService.setVariableLocal(processInstanceId, "receiverType", receiverType);
		processNodeService.setVariableLocal(processInstanceId, "receiverName", receiverName);
		processNodeService.setVariableLocal(processInstanceId, "receiverCity", receiverCity);
		processNodeService.setVariableLocal(processInstanceId, "deliveryStatus", "PENDING");
		JobOrder jobOrder = workDirective.getJobOrder();
		if(jobOrder != null) {
			processNodeService.setVariableLocal(processInstanceId, "workRequestId", jobOrder.getWorkRequest().getId());
		}
	}
}

