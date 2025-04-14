package com.mes.modules.manufacturingEngine.workflowNodes.setup.process;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.event.services.ArtifactEventService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Component
public class ProcessExecutionListenerStartEvent implements ExecutionListener {
	private static final long serialVersionUID = -1052662458234389116L;
	@Autowired
	private WorkflowNodeService runtimeExecutionService;
	@Autowired
	private ArtifactEventService artifactEventService;

	@Override
	public void notify(DelegateExecution execution) {
		this.setImplementationType(execution);
		ExecutionEntity processInstanceExecution = runtimeExecutionService.getExecution(execution.getId());
		if (processInstanceExecution.getSuperExecutionId() != null) {
			this.notifyCallActivity(processInstanceExecution);
		}
		this.initiate(execution);
	}

	private void setImplementationType(DelegateExecution execution) {
		execution.setVariableLocal(BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE, WorkflowNodeTypes.PROCESS.toString());
		String artifact = (String) execution.getVariableLocal(BaseWorkflowNode.PROP_ARTIFACT);
		if (artifact == null) {
			execution.setVariableLocal(BaseWorkflowNode.PROP_ARTIFACT, WorkflowNodeTypes.PROCESS.toString());
		}
	}

	private void initiate(DelegateExecution execution) {
		String processId =execution.getProcessInstanceId();
		ExecutionEvent event = artifactEventService.buildEvent(Long.valueOf(processId),
				EventEntities.PROCESS, null, Commands.INITIATE);
		artifactEventService.throwEvent(event);
	}

	private void notifyCallActivity(ExecutionEntity execution) {
		ExecutionEntity callActivityExecution = execution.getSuperExecution();
		callActivityExecution.setVariableLocal(BaseWorkflowNode.PROP_ROUTINE_ID, execution.getProcessInstanceId());
	}
}
