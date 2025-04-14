package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.variable.api.event.FlowableVariableEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Component
public class ArtifactStateChangedListener implements FlowableEventListener {
	@Autowired
	private WorkflowNodeService runtimeExecutionService;
	@Autowired
	private ArtifactTimeService artifactTimeService;

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableVariableEvent variableEvent = (FlowableVariableEvent) event;
		String VariableName = variableEvent.getVariableName();
		if (VariableName.equals(BaseWorkflowNode.PROP_STATE)) {
			String executionId = variableEvent.getExecutionId();
			Artifact artifact = runtimeExecutionService.getArtifact(executionId);
			WorkflowNode workflowNode = artifact.getArtifactService().buildWorkflowNodeByExecutionId(executionId);
			workflowNode.setArtifact(artifact);
			States state = States.valueOf((String) variableEvent.getVariableValue());
			artifactTimeService.setArtifactTimesBasedOnState(workflowNode, state);
			this.reflectStateToRootProcess(workflowNode,state);
		}
	}	

	private void reflectStateToRootProcess(WorkflowNode workflowNode,States state) {		
		if (state == States.RUNNING || state == States.HELD || state == States.ABORTED || state == States.STOPPED) {
			workflowNode.getArtifact().getArtifactService().reflectStateToRootProcess(workflowNode,state);
		}
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}
}
