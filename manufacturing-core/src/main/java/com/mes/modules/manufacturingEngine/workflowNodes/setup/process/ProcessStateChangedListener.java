package com.mes.modules.manufacturingEngine.workflowNodes.setup.process;

import org.flowable.engine.common.api.delegate.event.FlowableEvent;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.variable.api.event.FlowableVariableEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.BaseJobOrderParameters;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.dom.workSchedule.JobOrderParameter;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;
import com.mes.modules.workSchedule.services.JobOrderParameterRepository;

@Component
public class ProcessStateChangedListener implements FlowableEventListener {
	@Autowired
	private WorkflowNodeService runtimeExecutionService;
	@Autowired
	private JobOrderParameterRepository jobOrderParameterRepository;

	@Override
	public void onEvent(FlowableEvent event) {
		FlowableVariableEvent variableEvent = (FlowableVariableEvent) event;
		String VariableName = variableEvent.getVariableName();
		if (VariableName.equals(BaseWorkflowNode.PROP_STATE)) {
			States state = States.valueOf((String) variableEvent.getVariableValue());
			String executionId = variableEvent.getExecutionId();
			WorkflowNodeTypes nodeType = runtimeExecutionService.getWorkflowNodeType(executionId);
			if (nodeType == WorkflowNodeTypes.PROCESS) {
				if (this.getWorkType(executionId) != null) {
					this.handleMOMStateChanged(executionId, state);
				}
			}
		}
	}

	private WorkTypes getWorkType(String executionId) {
		Artifact artifact = runtimeExecutionService.getArtifact(executionId);
		String workTypeString = (String) artifact.getArtifactService().getVariableLocal(executionId,
				BaseWorkflowNode.PROP_WORK_TYPE);
		if (workTypeString != null) {
			WorkTypes workType = WorkTypes.valueOf(workTypeString);
			return workType;
		} else {
			return null;
		}
	}

	private void handleMOMStateChanged(String executionId, States state) {
		WorkDirective workdirective = (WorkDirective) runtimeExecutionService.getVariableLocal(executionId, "workDirective");
		JobOrder jobOrder = workdirective.getJobOrder();
		if (state == States.READY) {
			if (jobOrder != null) {
				JobOrderParameter currentDirectiveParameter = jobOrder
						.getParameter(BaseJobOrderParameters.PARAM_CURRENT_DIRECTIVE);
				if (currentDirectiveParameter == null) {
					currentDirectiveParameter = new JobOrderParameter(BaseJobOrderParameters.PARAM_CURRENT_DIRECTIVE,
							workdirective.getId().toString(), jobOrder);
				} else {
					currentDirectiveParameter.setValue(workdirective.getId().toString());
				}
				jobOrderParameterRepository.save(currentDirectiveParameter);
			}
		} else if (state == States.COMPLETE || state == States.ABORTED || state == States.STOPPED) {
			if (jobOrder != null) {
				JobOrderParameter jobOrderParameter = jobOrder
						.getParameter(BaseJobOrderParameters.PARAM_CURRENT_DIRECTIVE);
				jobOrderParameter.setValue(null);
				jobOrderParameterRepository.save(jobOrderParameter);
			}
		}

	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}
}
