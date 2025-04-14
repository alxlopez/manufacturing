package com.mes.modules.manufacturingEngine.history.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;
import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeQueryRequest;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeVariable;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Service
public class ProcessHistoryService {
	@Autowired
	private HistoryService historyService;
	@Autowired
	private WorkflowNodeHistoryService workflowNodeHistoryService;
	@Autowired
	private UserTaskHistoryService userTaskHistoryService;

	public List<HistoricWorkflowNode> getHistoricProcesses(WorkflowNodeQueryRequest request) {
		List<HistoricWorkflowNode> HistoricWorkflowNodes = new ArrayList<HistoricWorkflowNode>();
		HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
		if (request.getState() != null) {
			query.variableValueEquals(BaseWorkflowNode.PROP_STATE, request.getState().toString());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getInitiateTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_INITIATE_TIME, request.getInitiateTime());
		}
		if (request.getBeginInitiateTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_INITIATE_TIME, request.getBeginInitiateTime());
		}
		if (request.getFinalInitiateTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_INITIATE_TIME, request.getFinalInitiateTime());
		}
		/* startTime*/
		if (request.getBeginStartTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_START_TIME, request.getBeginStartTime());
		}
		if (request.getFinalStartTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_START_TIME, request.getFinalStartTime());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getEndTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getEndTime());
		}
		if (request.getBeginEndTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getBeginEndTime());
		}
		if (request.getFinalEndTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getFinalEndTime());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getScheduledStartTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_START_TIME, request.getScheduledStartTime());
		}
		if (request.getBeginScheduledStartTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_START_TIME, request.getBeginScheduledStartTime());
		}
		if (request.getFinalScheduledStartTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_START_TIME, request.getFinalScheduledStartTime());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getScheduledEndTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_END_TIME, request.getScheduledEndTime());
		}
		if (request.getBeginScheduledEndTime() != null) {
			query.variableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_END_TIME, request.getBeginScheduledEndTime());
		}
		if (request.getFinalScheduledEndTime() != null) {
			query.variableValueLessThanOrEqual(BaseWorkflowNode.PROP_SCHEDULED_END_TIME, request.getFinalScheduledEndTime());
		}
		if (request.getId() != null) {
			query.processInstanceId(request.getId());
		}
		if (request.getProcessInstanceId() != null) {
			query.processInstanceId(request.getProcessInstanceId());
		}
		if (request.getProcessDefinitionName() != null) {
			query.processDefinitionName(request.getProcessDefinitionName());
		}
		if (request.getName() != null) {
			query.processDefinitionName(request.getName());
		}
		if (request.getVariables() != null) {
			List<WorkflowNodeVariable> variables = request.getVariables();
			for(WorkflowNodeVariable variable: variables)	{
				if(variable.getName()!= null && variable.getValue() != null) {				
					query.variableValueEquals(variable.getName(), variable.getValue());
				}
			}
		}
		List<HistoricProcessInstance> historicProcessInstances = query.list();
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			HistoricWorkflowNode historicWorkflowNode = this.buildHistoricProcess(historicProcessInstance);
			HistoricWorkflowNodes.add(historicWorkflowNode);
		}
		return HistoricWorkflowNodes;
	}

	public HistoricWorkflowNode getHistoricProcess(String id) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
		.processInstanceId(id).singleResult();
		return this.buildHistoricProcess(historicProcessInstance);
	}

	public HistoricWorkflowNode getHistoricProcessJournal(String processId) {
		HistoricWorkflowNode historicWorkflowNode = this.getHistoricProcess(processId);
		List<HistoricWorkflowNode> historicUserTasks = this.getProcessHistoricUserTasks(processId);
		historicWorkflowNode.getActivities().addAll(historicUserTasks);
		return historicWorkflowNode;
	}

	private HistoricWorkflowNode buildHistoricProcess(HistoricProcessInstance historicProcessInstance) {
		HistoricWorkflowNode historicWorkflowNode = new HistoricWorkflowNode();
		List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().processInstanceId(historicProcessInstance.getId()).excludeTaskVariables().list();
		historicWorkflowNode.setId(historicProcessInstance.getId());
		historicWorkflowNode.setExecutionId(historicProcessInstance.getId());
		historicWorkflowNode.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
		historicWorkflowNode.setName(historicProcessInstance.getProcessDefinitionName());
		historicWorkflowNode.setProcessInstanceId(historicProcessInstance.getId());
		HashMap<String, Object> variables = new HashMap<String, Object>();
		for (HistoricVariableInstance var: historicVariables) {
			if(var.getVariableName().equals("scheduledStartTime") || var.getVariableName().equals("scheduledEndTime")) {
				if(var.getValue()!=null) {
					Date date = (Date) var.getValue();
					SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					String stringDate = f.format(date);
				    variables.put(var.getVariableName(), stringDate);
				} else {
					System.out.println("Historic Process Instance: "+historicProcessInstance.getId());
					variables.put(var.getVariableName(), null);
				}
 			} else if(var.getVariableName().equals("workDirective")) {
				WorkDirective workDirective = (WorkDirective) var.getValue();
				List<WorkDirectiveMaterialSpecification> workDirectiveMaterialSpecifications = workDirective.getMaterialSpecifications(MaterialUse.PRODUCED);
				Float totalQuantity = 0F;
				for(WorkDirectiveMaterialSpecification marerialSpecification : workDirectiveMaterialSpecifications) {
					Float quantity = marerialSpecification.getQuantity();
					totalQuantity += quantity;
				}
				variables.put("quantity", totalQuantity);
			} else {
				variables.put(var.getVariableName(), var.getValue());
			}
		}
		workflowNodeHistoryService.setBasedVariablesAtributes(historicWorkflowNode, variables);
		return historicWorkflowNode;

	}

	public List<HistoricWorkflowNode> getProcessHistoricUserTasks(String processId) {
		List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
		.processInstanceId(processId).list();		
		List<HistoricWorkflowNode> processHistoricTasks= new ArrayList<HistoricWorkflowNode>();
		for (HistoricTaskInstance historicTaskInstance : historicTasks) {
			HistoricWorkflowNode HistoricWorkflowNode = userTaskHistoryService
					.getWorkflowNode(historicTaskInstance);
			processHistoricTasks.add(HistoricWorkflowNode);
		}
		return processHistoricTasks;
	}
}
