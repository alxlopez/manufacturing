package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.context.Context;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class UserTaskListenerCreateEvent implements TaskListener {
	private static final long serialVersionUID = -2153410674392880282L;
	@Autowired
	private RuntimeService runtimeService;

	@Override
	public void notify(DelegateTask delegateTask) {	
		this.initiate(delegateTask);
		this.assignTask(delegateTask);
	}	

	private void assignTask(DelegateTask delegateTask) {
		Object assignee = runtimeService.getVariable(delegateTask.getExecutionId(), "assignee");
		Object candidateGroup = runtimeService.getVariable(delegateTask.getExecutionId(), "candidateGroup");
		if (assignee != null) {
			TaskService taskService = Context.getProcessEngineConfiguration().getTaskService();
			taskService.claim(delegateTask.getId(), assignee.toString());
		} else if (candidateGroup != null) {
			delegateTask.addCandidateGroup(candidateGroup.toString());
		}
	}

	private void initiate(DelegateTask delegateTask){
		delegateTask.setVariableLocal(BaseWorkflowNode.PROP_STATE, States.READY.toString());
	}
}
