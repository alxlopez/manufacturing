package com.mes.modules.manufacturingEngine.workflowNodes;

import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventType;
import org.flowable.engine.delegate.DelegateExecution;

import com.mes.dom.enumerations.application.WorkflowNodeEventTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;

import lombok.Data;

@Data
public class WorkflowNodeEvent implements FlowableEvent {	
	protected String workflowNodeId;
	protected String executionId;
	protected String processInstanceId;
	protected WorkflowNodeTypes workflowNodeType;	
	protected WorkflowNodeEventTypes eventType;
	protected Object eventValue;
	protected Artifact workflowNodeArtifact;
	protected DelegateExecution delegateExecution;
	protected String processDefinitionId;	
	static FlowableEventType type = FlowableEngineEventType.CUSTOM;

	@Override
	public FlowableEventType getType() {
		return type;
	}
}
