package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class UserTaskExecutionListenerEndEvent implements ExecutionListener{
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) { }
}
