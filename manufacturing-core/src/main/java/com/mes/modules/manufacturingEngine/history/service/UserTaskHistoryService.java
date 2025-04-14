package com.mes.modules.manufacturingEngine.history.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.MaterialUse;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workDirective.WorkDirectiveMaterialSpecification;
import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeQueryRequest;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeVariable;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.workDirective.services.WorkDirectiveRepository;

@Service
public class UserTaskHistoryService {
	@Autowired
	private HistoryService historyService;
	@Autowired
	private WorkflowNodeHistoryService workflowNodeHistoryService;
	@Autowired
	private WorkDirectiveRepository workDirectiveRepository;

	public List<HistoricWorkflowNode> getWorkflowNodes(WorkflowNodeQueryRequest request) {
		List<HistoricWorkflowNode> HistoricWorkflowNodes = new ArrayList<HistoricWorkflowNode>();
		HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery();
		if (request.getId() != null) {
			query.taskId(request.getId());
		}
		if (request.getName() != null) {
			query.taskName(request.getName());
		}
		if (request.getState() != null) {
			query.taskVariableValueEquals(BaseWorkflowNode.PROP_STATE, request.getState().toString());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getInitiateTime() != null) {
			query.taskVariableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_START_TIME, request.getInitiateTime());
		}
		if (request.getBeginInitiateTime() != null) {
			query.taskVariableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_INITIATE_TIME, request.getBeginInitiateTime());
		}
		if (request.getFinalInitiateTime() != null) {
			query.taskVariableValueLessThanOrEqual(BaseWorkflowNode.PROP_INITIATE_TIME, request.getFinalInitiateTime());
		}
		if (request.getBeginStartTime() != null) {
			query.taskVariableValueGreaterThanOrEqual(BaseWorkflowNode.PROP_START_TIME, request.getBeginStartTime());
		}
		if (request.getFinalStartTime() != null) {
			query.taskVariableValueLessThanOrEqual(BaseWorkflowNode.PROP_START_TIME, request.getFinalStartTime());
		}
		//Eliminar éste bloque If después de cambios
		if (request.getEndTime() != null) {
			query.taskVariableValueLessThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getEndTime());
		}
		if (request.getBeginEndTime() != null) {
			query.taskVariableValueLessThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getBeginEndTime());
		}
		if (request.getFinalEndTime() != null) {
			query.taskVariableValueLessThanOrEqual(BaseWorkflowNode.PROP_END_TIME, request.getFinalEndTime());
		}
		if (request.getProcessInstanceId() != null) {
			query.processInstanceId(request.getProcessInstanceId());
		}
		if (request.getProcessDefinitionName() != null) {
			query.processDefinitionName(request.getProcessDefinitionName());
		}		
		if (request.getAssignee() != null) {
			query.taskAssignee(request.getAssignee());
		}
		if (request.getVariables() != null) {
			List<WorkflowNodeVariable> variables = request.getVariables();
			for(WorkflowNodeVariable variable: variables)	{
				if(variable.getName()!= null && variable.getValue() != null)
					query.taskVariableValueEquals(variable.getName(), variable.getValue());
			}
		}
		List<HistoricTaskInstance> historicInstances = query.list();
		for (HistoricTaskInstance historicTaskInstance : historicInstances) {
			HistoricWorkflowNode historicWorkflowNode = this.getWorkflowNode(historicTaskInstance);
			HistoricWorkflowNodes.add(historicWorkflowNode);
		}
		return HistoricWorkflowNodes;
	}

	public HistoricWorkflowNode getWorkflowNode(HistoricTaskInstance historicTaskInstance) {
		List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).list();
		String processInstanceId = historicTaskInstance.getProcessInstanceId();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
		.processInstanceId(processInstanceId).singleResult();
		HistoricWorkflowNode historicWorkflowNode = new HistoricWorkflowNode();
		historicWorkflowNode.setId(historicTaskInstance.getId());
		historicWorkflowNode.setExecutionId(historicTaskInstance.getExecutionId());
		historicWorkflowNode.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
		historicWorkflowNode.setName(historicTaskInstance.getName());
		historicWorkflowNode.setProcessInstanceId(historicProcessInstance.getId());
		HashMap<String, Object> variables = new HashMap<String, Object>();
		for (HistoricVariableInstance var: historicVariables) {
			variables.put(var.getVariableName(), var.getValue());
		}
		List<WorkDirective> workDirectiveList = workDirectiveRepository.findByWorkflowSpecificationId(processInstanceId);
		if(workDirectiveList != null && workDirectiveList.size() > 0){
			WorkDirective workDirective = workDirectiveList.get(0);
			variables.put("workDirectiveId", workDirective.getId());
			JobOrder jobOrder = workDirective.getJobOrder();
			if(jobOrder != null){
				Long workRequestId = jobOrder.getWorkRequest().getId();
				variables.put("workRequestId", workRequestId);
			}
			List<WorkDirectiveMaterialSpecification> workDirectiveMaterialSpecifications = workDirective.getMaterialSpecifications(MaterialUse.PRODUCED);
			Float totalQuantity = 0F;
			for(WorkDirectiveMaterialSpecification marerialSpecification : workDirectiveMaterialSpecifications) {
				Float quantity = marerialSpecification.getQuantity();
				totalQuantity += quantity;
			}
			variables.put("quantity", totalQuantity);
		}
		variables.put("artifact", ArtifactTypes.USER_TASK.toString());
		workflowNodeHistoryService.setBasedVariablesAtributes(historicWorkflowNode, variables);
		historicWorkflowNode.setAssignee(historicTaskInstance.getAssignee());
		return historicWorkflowNode;
	}
}
