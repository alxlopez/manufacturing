package com.mes.modules.milanes.artifacts;



import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.personnel.Person;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.personnel.services.PersonRepository;

@Component
public class ProcessInitialParametrization implements JavaDelegate {
	@Autowired
	private ProcessArtifactService processNodeService;
	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public void execute(DelegateExecution execution) {
		String processInstanceId = execution.getProcessInstanceId();
		WorkDirective workDirective = (WorkDirective) processNodeService.getVariableLocal(
				processInstanceId,
				"workDirective"
		);
		JobOrder jobOrder = (JobOrder) processNodeService.getVariableLocal(processInstanceId,"jobOrder");
		String customerId = workDirective.getParameter("idCustomer").getValue();
		String patient = workDirective.getParameter("patientCustomer").getValue();
		String workRequestId = Long.toString(jobOrder.getWorkRequest().getId());
		Person person = personRepository.findById(Long.parseLong(customerId));
		String customerName = person.getDescription();
		Date scheduledStartTime = new Date(jobOrder.getStartTime().getTime());
		processNodeService.setVariableLocal(processInstanceId, "scheduledStartTime",scheduledStartTime);
		if (jobOrder.getEndTime() != null) {
			Date scheduledEndTime = new Date(jobOrder.getEndTime().getTime());
			processNodeService.setVariableLocal(processInstanceId,"scheduledEndTime",scheduledEndTime);
		}
		processNodeService.setVariableLocal(processInstanceId, "customerId", customerId);
		processNodeService.setVariableLocal(processInstanceId, "customerPatient", patient);
		processNodeService.setVariableLocal(processInstanceId, "customerName", customerName);
		processNodeService.setVariableLocal(processInstanceId, "workRequestId", workRequestId);
	}
}
