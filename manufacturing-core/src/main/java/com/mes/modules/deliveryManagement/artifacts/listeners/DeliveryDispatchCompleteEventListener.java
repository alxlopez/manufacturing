package com.mes.modules.deliveryManagement.artifacts.listeners;

import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.deliveryManagement.BaseDelivery;
import com.mes.modules.manufacturingEngine.event.services.ArtifactEventService;

@Component
public class DeliveryDispatchCompleteEventListener implements TaskListener {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ArtifactEventService artifactEventService;

	@Override
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		RuntimeService runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
		.variableValueEquals(BaseDelivery.PROP_SUPER_DELIVERY_ID, processInstanceId).list();
		for (ProcessInstance processInstance : processInstances) {			
			TaskService serviceTask = Context.getProcessEngineConfiguration().getTaskService();
			Task task = serviceTask.createTaskQuery().processInstanceId(processInstance.getId())
			.taskName(delegateTask.getName()).singleResult();
			if (task != null) {
				ExecutionEvent event = artifactEventService.buildEvent(Long.valueOf(task.getId()), EventEntities.USER_TASK,
						delegateTask.getAssignee(), Commands.START);
				artifactEventService.throwEvent(event);
			}
		}
	}
}
