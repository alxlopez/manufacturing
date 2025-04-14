package com.mes.modules.milanes.artifacts;

import java.util.Date;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.milanes.services.RulesService;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;


@Service
public class ScheduleEndTime implements JavaDelegate {
	@Autowired
	private RulesService ruleService;
	@Autowired
	private ProcessArtifactService workflowService;
	@Autowired
	private WorkDirectiveRepository workDirectiveRepository;

	@Override
	public void execute(DelegateExecution execution) {
		Date startDate = ruleService.getInitialDate();
		Date endDate = ruleService.getFinishDate(startDate, 3);
		Date scheduledStartTime = ruleService.setTimeDateParameters(startDate, 7, 0, 0);
		Date scheduledEndTime = ruleService.setTimeDateParameters(endDate, 12, 0, 0);
		String processInstanceId = execution.getProcessInstanceId();
		workflowService.setVariableLocal(processInstanceId, "scheduledStartTime", scheduledStartTime);
		workflowService.setVariableLocal(processInstanceId, "scheduledEndTime", scheduledEndTime);
		WorkDirective workDirective =(WorkDirective) workflowService.getVariable(processInstanceId, "workDirective");
		Long jobOrderId = (Long) workflowService.getVariable(processInstanceId, "jobOrderId");
		String dispatchCity = ruleService.getDispatchCity(jobOrderId);
		workDirective.getParameter("dispatchCity").setValue(dispatchCity);
		workDirectiveRepository.save(workDirective);
	}
}
