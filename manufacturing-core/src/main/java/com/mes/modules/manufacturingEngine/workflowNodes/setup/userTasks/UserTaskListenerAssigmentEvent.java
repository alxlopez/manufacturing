package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component
public class UserTaskListenerAssigmentEvent implements TaskListener {
	private static final long serialVersionUID = -2153410674392880282L;

	@Override
	public void notify(DelegateTask delegateTask) { }
}
