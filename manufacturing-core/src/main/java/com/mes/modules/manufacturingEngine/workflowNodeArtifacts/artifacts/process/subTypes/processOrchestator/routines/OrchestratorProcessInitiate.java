package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.routines;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.EventEntities;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.workDirective.BaseWorkDirective;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveParameter;
import com.mes.modules.manufacturingEngine.event.services.WorkDirectiveEventService;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.routines.ProcessInitiate;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;

@Component
public class OrchestratorProcessInitiate extends ProcessInitiate {
	@Autowired
	private WorkDirectiveRepository WorkDirectiveRepository;
	@Autowired
	private WorkflowNodeService workflowNodeService;
	@Autowired
	private WorkDirectiveEventService workDirectiveEventService;

	@Override
	public void execute(WorkflowNode workflowNode, ExecutionEvent event) {
		ArtifactService artifactService = workflowNode.getArtifact().getArtifactService();
		WorkDirective workDirective = (WorkDirective) artifactService.getVariableLocal(workflowNode.getArtifactId(),
				BaseWorkflowNode.PROP_WORK_DIRECTIVE);
		Collection<WorkDirective> referencedWorkDirectives = this.getReferencedWorkDirectives(workDirective);
		this.initiateReferencedWorkDirectives(workDirective, referencedWorkDirectives);		
		this.SetOwnerToReferencedDirectives(workflowNode.getExecutionId(), referencedWorkDirectives);
		super.execute(workflowNode, event);
	}

	private Collection<WorkDirective> getReferencedWorkDirectives(WorkDirective workDirective) {
		Collection<WorkDirective> referencedWorkDirectives = new HashSet<WorkDirective>();
		WorkDirectiveParameter referencedDirectivesIdsParam = workDirective
				.getParameter(BaseWorkDirective.PROP_REFERENCED_DIRECTIVE_IDS);
		if (referencedDirectivesIdsParam != null) {
			Collection<WorkDirectiveParameter> referencedDirectivesIds = referencedDirectivesIdsParam.getParameters();
			for (WorkDirectiveParameter directiveId : referencedDirectivesIds) {
				Long workDirectiveId = Long.valueOf(directiveId.getValue());
				WorkDirective referencedWorkDirective = WorkDirectiveRepository.findById(workDirectiveId);
				referencedWorkDirectives.add(referencedWorkDirective);
			}
		}
		return referencedWorkDirectives;
	}

	private void initiateReferencedWorkDirectives(WorkDirective workDirective,
			Collection<WorkDirective> referencedWorkDirectives) {
		for (WorkDirective referencedWorkDirective : referencedWorkDirectives) {
			String referencedWorkflowSpecificationId = referencedWorkDirective.getWorkflowSpecificationId();
			if (referencedWorkflowSpecificationId == null) {
				ExecutionEvent workDirectiveEvent = workDirectiveEventService.buildEvent(
						referencedWorkDirective.getId(), EventEntities.WORK_DIRECTIVE, null, Commands.INITIATE);
				workDirectiveEventService.throwEvent(workDirectiveEvent);
				referencedWorkflowSpecificationId = referencedWorkDirective.getWorkflowSpecificationId();
			}
		}
	}

	private void SetOwnerToReferencedDirectives(String ownerWorkflowSpecificationId,
			Collection<WorkDirective> referencedDirectives) {		
		for (WorkDirective referencedDirective : referencedDirectives) {
			String referencedWorkflowSpecificationId = referencedDirective.getWorkflowSpecificationId();
			workflowNodeService.setVariableLocal(referencedWorkflowSpecificationId, BaseWorkflowNode.PROP_OWNER_ID,
					ownerWorkflowSpecificationId);
		}
	}
}
