package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.subProcess.routines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.event.ExecutionEvent;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.routines.WorkflowNodeArtifactRoutineStopping;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.SubprocessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class SubProcessStopping extends WorkflowNodeArtifactRoutineStopping {
	@Autowired
	private SubprocessArtifactService  subProcessService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		String subProcessId = subProcessService.getRoutineId(workflowNode.getArtifactId());
		eventService.throwEvent(Long.valueOf(subProcessId), EventEntities.PROCESS, Commands.STOP);		
	}
}
