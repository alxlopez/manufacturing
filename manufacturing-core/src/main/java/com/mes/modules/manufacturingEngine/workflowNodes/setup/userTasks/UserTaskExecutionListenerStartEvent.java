package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class UserTaskExecutionListenerStartEvent implements ExecutionListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		this.setImplementationType(execution);
	}

	private void setImplementationType(DelegateExecution execution) {
		execution.setVariableLocal(BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE, WorkflowNodeTypes.USER_TASK.toString());
		String artifact = (String) execution.getVariableLocal(BaseWorkflowNode.PROP_ARTIFACT);
		if (artifact == null) {
			execution.setVariableLocal(BaseWorkflowNode.PROP_ARTIFACT, ArtifactTypes.USER_TASK.toString());
		}
	}
}
