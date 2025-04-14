package com.mes.modules.manufacturingEngine.workflowNodes.setup.subProcess;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CallActivityEndListener implements ExecutionListener {
	private static final long serialVersionUID = -2153410674392880282L;

	@Override
	public void notify(DelegateExecution execution) { }
}
