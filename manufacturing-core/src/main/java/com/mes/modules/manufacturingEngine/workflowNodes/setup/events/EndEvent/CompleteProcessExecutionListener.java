package com.mes.modules.manufacturingEngine.workflowNodes.setup.events.EndEvent;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;

@Component
public class CompleteProcessExecutionListener implements ExecutionListener {
	private static final long serialVersionUID = 5850865848306405240L;	
	@Autowired
	private BeanFactory beanFactory;

	@Override
	public void notify(DelegateExecution execution) {
		ProcessArtifactService artifactService = beanFactory.getBean(ProcessArtifactService.class);
		artifactService.setState(execution.getProcessInstanceId(), States.COMPLETE);;
	}
}
