package com.mes.modules.manufacturingEngine.history.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Service
public class WorkflowNodeHistoryService {
	@Autowired
	private HistoryService historyService;

	public HistoricWorkflowNode buildHistoricActivity(String executionId) {
		HistoricWorkflowNode historicBaseWorkflowNode = this.buildBaseHistoricWorkflowNode(executionId);
		Map<String, Object> variables = getHistoricExecutionVariables(executionId);
		HistoricWorkflowNode historicWorkflowNode = this.setBasedVariablesAtributes(historicBaseWorkflowNode, variables);
		historicWorkflowNode.setVariables(variables);
		return historicWorkflowNode;
	}

	public Map<String, Object> getHistoricExecutionVariables(String executionId) {
		List<HistoricVariableInstance> activityInstanceVariables = historyService.createHistoricVariableInstanceQuery()
		.executionId(executionId).list();
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		for (HistoricVariableInstance historicVariableInstance : activityInstanceVariables) {
			variablesMap.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
		}
		return variablesMap;
	}

	private HistoricWorkflowNode buildBaseHistoricWorkflowNode(String executionId) {
		HistoricActivityInstance activityInstance = historyService.createHistoricActivityInstanceQuery()
		.executionId(executionId).singleResult();
		String processInstanceId = activityInstance.getProcessInstanceId();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
		.processInstanceId(processInstanceId).singleResult();
		HistoricWorkflowNode historicWorkflowNode = new HistoricWorkflowNode();
		historicWorkflowNode.setId(activityInstance.getId());
		historicWorkflowNode.setExecutionId(activityInstance.getExecutionId());
		historicWorkflowNode.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
		historicWorkflowNode.setProcessInstanceId(historicProcessInstance.getId());
		return historicWorkflowNode;
	}

	public HistoricWorkflowNode setBasedVariablesAtributes(HistoricWorkflowNode historicWorkflowNode,
			Map<String, Object> variables) {
		Object stateObject = variables.get(BaseWorkflowNode.PROP_STATE);
		if (stateObject != null) {
			States state = States.valueOf(((String) stateObject));
			historicWorkflowNode.setState(state);
			variables.remove(BaseWorkflowNode.PROP_STATE);
		}
		Object initiateTime = variables.get(BaseWorkflowNode.PROP_INITIATE_TIME);
		if (initiateTime != null) {
			historicWorkflowNode.setInitiateTime((Date) initiateTime);
			variables.remove(BaseWorkflowNode.PROP_INITIATE_TIME);
		}
		Object startTime = variables.get(BaseWorkflowNode.PROP_START_TIME);
		if (startTime != null) {
			historicWorkflowNode.setStartTime((Date) startTime);
			variables.remove(BaseWorkflowNode.PROP_START_TIME);
		}
		Object endTime = variables.get(BaseWorkflowNode.PROP_END_TIME);
		if (endTime != null) {
			historicWorkflowNode.setEndTime((Date) endTime);
			variables.remove(BaseWorkflowNode.PROP_END_TIME);
		}
		Object enlapsedTime = variables.get(BaseWorkflowNode.PROP_ENLAPSED_TIME);
		if (enlapsedTime != null) {
			historicWorkflowNode.setEnlapsedTime((Long) enlapsedTime);
			variables.remove(BaseWorkflowNode.PROP_ENLAPSED_TIME);
		}
		Object artifactObject = variables.get(BaseWorkflowNode.PROP_ARTIFACT);
		if (artifactObject != null) {
			ArtifactTypes artifactType = ArtifactTypes.valueOf(((String) artifactObject));
			historicWorkflowNode.setArtifact(artifactType);
			variables.remove(BaseWorkflowNode.PROP_ARTIFACT);
		}
		Object workDirectiveObject = variables.get(BaseWorkflowNode.PROP_WORK_DIRECTIVE);
		if (workDirectiveObject != null) {
			variables.remove(BaseWorkflowNode.PROP_WORK_DIRECTIVE);			
		}
		Object jobOrderObject = variables.get(BaseWorkflowNode.PROP_JOB_ORDER);
		if (jobOrderObject != null) {
			variables.remove(BaseWorkflowNode.PROP_JOB_ORDER);			
		}
		historicWorkflowNode.setVariables(variables);
		return historicWorkflowNode;
	}
}
