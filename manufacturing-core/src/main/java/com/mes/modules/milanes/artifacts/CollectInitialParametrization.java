package com.mes.modules.milanes.artifacts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.personnel.Person;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.personnel.services.PersonRepository;

@Component
public class CollectInitialParametrization implements JavaDelegate {
	@Autowired
	private ProcessArtifactService processNodeService;
	@Autowired
	private PersonRepository personRepository;

	@Override
	public void execute(DelegateExecution execution) {
		try {
			String processInstanceId = execution.getProcessInstanceId();
			WorkDirective workDirective = (WorkDirective) processNodeService.getVariableLocal(
					processInstanceId,
					"workDirective"
			);
			String senderId = workDirective.getParameter("senderId").getValue();
			String senderAgentType = workDirective.getParameter("senderAgentType").getValue();
			String date = workDirective.getParameter("collectDate").getValue();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date collectDate = null;
			collectDate = sdf.parse(date);
			Person person = personRepository.findById(Long.parseLong(senderId));
			String senderName = person.getDescription();
			String senderDocument = person.getCode();
			processNodeService.setVariableLocal(processInstanceId, "senderId", senderId);
			processNodeService.setVariableLocal(processInstanceId, "senderAgentType", senderAgentType);
			processNodeService.setVariableLocal(processInstanceId, "senderName", senderName);
			processNodeService.setVariableLocal(processInstanceId, "collectStatus", "PENDING");
			processNodeService.setVariableLocal(processInstanceId, "assignee", null);
			processNodeService.setVariableLocal(processInstanceId, "collectDate", collectDate);
			processNodeService.setVariableLocal(processInstanceId, "senderDocument", senderDocument);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
