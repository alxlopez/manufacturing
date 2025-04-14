package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states;

import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class WorkflowNodeStoppingState extends WorkflowNodeArtifactState {
	@Override
	public void abort(WorkflowNode workflowNode, ExecutionEvent event) {
		workflowNode.getArtifact().getAbortingRoutine().execute(workflowNode, event);
	}

	@Override
	public void complete(WorkflowNode workflowNode, ExecutionEvent event) {
		Artifact workflowNodeArtifact = workflowNode.getArtifact();
		workflowNodeArtifact.getStoppingRoutine().completeRoutine(workflowNode, event);
		workflowNodeArtifact.getArtifactService().setVariableLocal(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_STATE, States.STOPPED.toString());
	}
}
