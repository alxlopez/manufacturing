package com.mes.modules.manufacturingEngine.commandProcessors.workDirective;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.config.exception.ManufacturingConflictException;
import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.dom.enumerations.application.Commands;
import com.mes.dom.enumerations.application.WorkflowNodeBpmCategories;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services.ProcessArtifactService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.workDirective.controller.exceptions.WorkDirectiveExceptionCodes;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;
import com.mes.modules.workDirective.services.WorkDirectiveService;

@Component
public class WorkDirectiveCommandProcessor  {
	@Autowired
	protected WorkDirectiveService workDirectiveService;
	@Autowired
	protected WorkDirectiveRepository workDirectiveRepository;
	@Autowired
	protected ProcessArtifactService workflowService;
	@Autowired
	protected BeanFactory beanFactory;

	public void execute(ExecutionEvent event) {
		WorkDirective workDirective = workDirectiveService.validateWorkDirective(event.getEntityId());
		this.validateCommand(workDirective, event);
		this.executeCommand(workDirective, event);
		this.reportEventAction(workDirective, event);
	}

	protected void validateCommand(WorkDirective workDirective, ExecutionEvent event) {
		Commands command = event.getEventDefinition().getCode();
		if (command == Commands.INITIATE) {
			if (workDirective.getWorkflowSpecificationId() != null) {
				throw new ManufacturingConflictException(WorkDirectiveExceptionCodes.workDirectiveAlreadyInitiated);
			}
		} else {
			throw new ManufacturingIllegalArgumentException(WorkDirectiveExceptionCodes.unknownCommand);
		}
	}

	protected void executeCommand(WorkDirective workDirective, ExecutionEvent event) {
		Map<String, Object> variables = this.defineVariables(workDirective);
		this.createProcessInstance(workDirective, variables);
		this.reportEventAction(event, workDirective);
		workDirectiveRepository.save(workDirective);
	}

	protected void reportEventAction(WorkDirective workDirective, ExecutionEvent event) {
		event.setAction("WORKFLOW_INITIATED");
		event.setActionResult(workDirective.getWorkflowSpecificationId());
	}

	private Map<String, Object> defineVariables(WorkDirective workDirective) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(BaseWorkflowNode.PROP_WORK_DIRECTIVE, workDirective);
		variables.put(BaseWorkflowNode.PROP_WORK_DIRECTIVE_ID, workDirective.getId());
		variables.put(BaseWorkflowNode.PROP_BPM_CATEGORY, WorkflowNodeBpmCategories.MOM.toString());
		variables.put(BaseWorkflowNode.PROP_HIERARCHY_SCOPE_ID, workDirective.getHierarchyScope().getId()); 
		variables.put(BaseWorkflowNode.PROP_WORK_TYPE, workDirective.getWorkType().toString());
		if (workDirective.getJobOrder() != null) {
			JobOrder jobOrder = workDirective.getJobOrder();
			variables.put(BaseWorkflowNode.PROP_JOB_ORDER, jobOrder);
			variables.put(BaseWorkflowNode.PROP_JOB_ORDER_ID, workDirective.getJobOrder().getId());			
		}
		return variables;
	}

	private void createProcessInstance(WorkDirective workDirective, Map<String, Object> variables) {
		String workflowSpecificationId = workDirective.getWorkMaster().getWorkflowSpecificationId();
		String processInstanceInitiated = workflowService.startProcessInstanceByKey(workflowSpecificationId, variables);
		workDirective.setWorkflowSpecificationId(processInstanceInitiated);
	}

	private void reportEventAction(ExecutionEvent event, WorkDirective workDirective) {
		event.setAction("WORKFLOW_INITIATED");
		event.setActionResult(workDirective.getWorkflowSpecificationId());
	}
}
