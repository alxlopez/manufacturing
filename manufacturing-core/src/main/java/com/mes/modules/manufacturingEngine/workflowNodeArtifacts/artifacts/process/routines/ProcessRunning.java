package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines;

import org.springframework.stereotype.Component;

import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineRunning;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class ProcessRunning extends WorkflowNodeArtifactRoutineRunning {
	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) { }
}
