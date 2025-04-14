package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;


import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.variable.api.event.FlowableVariableEvent;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class UserTaskStateChangedListener implements FlowableEventListener {
	@Override
	public void onEvent(FlowableEvent event) {
		FlowableVariableEvent variableEvent = (FlowableVariableEvent) event;
		String VariableName = variableEvent.getVariableName();
		if (VariableName.equals(BaseWorkflowNode.PROP_STATE)) {
			String taskId = variableEvent.getTaskId();
			if (taskId != null) {
				
			}
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}
}
