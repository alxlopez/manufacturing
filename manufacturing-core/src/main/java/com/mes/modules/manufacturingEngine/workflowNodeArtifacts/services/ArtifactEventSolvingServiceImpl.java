package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.EventStatus;
import com.mes.dom.enumerations.application.WorkflowNodeBpmCategories;
import com.mes.dom.event.EventDirective;
import com.mes.dom.event.Failure;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.event.services.FailureService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.BaseArtifactEventSolving;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Service("artifactEventSolvingService")
public class ArtifactEventSolvingServiceImpl implements ArtifactEventSolvingService {
	@Autowired
	private WorkflowNodeService runtimeExecutionService;
	@Autowired
	private ProcessArtifactService processArtifactService;
	@Autowired
	private FailureService failureService;	

	public String createEventSolvingProcess(String artifactId,ArtifactTypes artifactType, Long failureId, String reporterId,String comment) {
		Artifact artifact = runtimeExecutionService.getArtifactObject(artifactType);		
		Failure failure = failureService.getFailure(Long.valueOf(failureId));		
		EventDirective eventDirective = getEventDirective(failure, reporterId,comment);
		Map<String, Object> variables = defineHoldingVariables(artifact,artifactId, eventDirective);
		String eventSolvingProcessId = processArtifactService.startProcessInstanceByKey(eventDirective.getEventMasterId(),
				variables);		
		return eventSolvingProcessId;	
	}

	private Map<String, Object> defineHoldingVariables(Artifact artifact, String artifactId,
			EventDirective eventDirective) {		
		WorkflowNode workflowNode = artifact.getArtifactService().buildWorkflowNodeByArtifactId(artifactId);		
		WorkDirective workDirective = (WorkDirective) artifact.getArtifactService().getVariable(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_WORK_DIRECTIVE);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(BaseArtifactEventSolving.PROP_FAILURED_NODE_EXECUTION_ID, workflowNode.getExecutionId());
		variables.put(BaseArtifactEventSolving.PROP_PARENT_PROCESS_ID, workflowNode.getExecutionId());
		variables.put(BaseArtifactEventSolving.PROP_FAILURE_ID, eventDirective.getFailure().getId());
		variables.put(BaseArtifactEventSolving.PROP_FAILURE_COMMENT, eventDirective.getFailureComment());
		variables.put(BaseArtifactEventSolving.PROP_ASSIGNEE, eventDirective.getAssignee());
		variables.put(BaseArtifactEventSolving.PROP_CANDIDATE_GROUP, eventDirective.getCandidateGroup());
		variables.put(BaseArtifactEventSolving.PROP_EVENT_STATUS, EventStatus.OPENED.toString());
		variables.put(BaseWorkflowNode.PROP_BPM_CATEGORY, WorkflowNodeBpmCategories.STANDARD_BPM.toString());
		if (workDirective != null) {			
			variables.put(BaseArtifactEventSolving.PROP_WORKDIRECTIVE_ID, workDirective.getId());
		}
		return variables;
	}

	private EventDirective getEventDirective(Failure failure, String reporterId, String comment) {
		EventDirective eventDirective = new EventDirective();
		eventDirective.setEventMasterId(failure.getEventMasterId());
		eventDirective.setFailure(failure);
		if (failure.getCandidateGroup() == null) {
			eventDirective.setAssignee(reporterId);
		} else {
			eventDirective.setCandidateGroup(failure.getCandidateGroup());
		}
		eventDirective.setFailureComment(comment);
		return eventDirective;
	}

	@Override
	public void setProcessAsChildExecution(String eventSolvingProcessId,String parentExecutionId) {
		runtimeExecutionService.setParentExecution(eventSolvingProcessId,parentExecutionId);		
	}

	public void cmdRemoveParentProcess(String parentProcessId,String reason){
		runtimeExecutionService.getEngineRuntimeService().deleteProcessInstance(parentProcessId, reason);		
	}
}
