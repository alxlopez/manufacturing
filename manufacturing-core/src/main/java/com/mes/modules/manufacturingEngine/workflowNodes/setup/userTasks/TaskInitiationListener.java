package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TaskInitiationListener implements TaskListener {
	private static final long serialVersionUID = 2159845417470649931L;
	private Expression equipmentBinding;

	@Override
	public void notify(DelegateTask delegateTask) {
		this.setEquipmentBinding(delegateTask);
	}

	private void setEquipmentBinding(DelegateTask delegateTask) {
		if (equipmentBinding != null) {
			String executionId = delegateTask.getExecutionId();
			DelegateExecution delegateExecution = CommandContextUtil.getExecutionEntityManager().findById(executionId);
			delegateTask.setVariableLocal("equipmentBinding",
					(String) equipmentBinding.getValue(delegateExecution));
		} else {
			delegateTask.setVariableLocal("equipmentBinding",null);
		}
	}
}
