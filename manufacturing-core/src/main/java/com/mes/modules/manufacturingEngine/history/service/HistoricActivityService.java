package com.mes.modules.manufacturingEngine.history.service;

import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeQueryRequest;

@Service
public class HistoricActivityService extends WorkflowNodeHistoryService {
	@Autowired
	HistoryService historyService;

	public List<HistoricWorkflowNode> getWorkflowNodes(WorkflowNodeQueryRequest request) {
		return null;
	}

	public HistoricWorkflowNode getWorkflowNode(String executionId) {		
		return super.buildHistoricActivity(executionId);
	}

	public List<HistoricWorkflowNode> getHistoricsProcessActities(String id) {
		List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
		.processInstanceId(id).list();
		for (HistoricActivityInstance historicActivityInstance : activities) {
			String executionId = historicActivityInstance.getExecutionId();
			@SuppressWarnings("unused")
			List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
					.executionId(executionId).list();			
		}
		return null;
	}
}
