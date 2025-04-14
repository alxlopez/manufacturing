package com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.commands.WorkflowNodeCommand;
import com.mes.modules.manufacturingEngine.stateMachines.workflowNodeStateMachine.states.WorkflowNodeArtifactState;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Service("workflowNodeMachineState")
public class WorkflowNodeStateMachineImpl implements WorkflowNodeStateMachine {
	@Autowired
	protected BeanFactory beanFactory;

	public void processEvent(WorkflowNode workflowNode, ExecutionEvent event) {
		Commands command = event.getEventDefinition().getCode();
		String nodeState = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, workflowNode.getState().toString());
		WorkflowNodeArtifactState workflowNodeState = (WorkflowNodeArtifactState) beanFactory
				.getBean("workflowNode" + nodeState + "State", WorkflowNodeArtifactState.class);
		String commandMethod = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, command.toString());
		WorkflowNodeCommand workflowNodeCommand = (WorkflowNodeCommand) beanFactory
				.getBean("workflowNode" + commandMethod + "Command", WorkflowNodeCommand.class);
		workflowNodeCommand.execute(workflowNodeState, workflowNode, event);
	}
}
