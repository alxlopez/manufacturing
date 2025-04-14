package com.mes.modules.manufacturingEngine.history.controller;

import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.ListHistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeQueryRequest;
import com.mes.modules.manufacturingEngine.history.service.UserTaskHistoryService;

@RestController
@RequestMapping("/mes/history/userTasks")
public class HistoricTaskController {
	@Autowired
	private UserTaskHistoryService userTaskHistoryService;
	@Autowired
	private HistoryService historyService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ListHistoricWorkflowNode getWorkflowNodes(@RequestBody WorkflowNodeQueryRequest workflowNodeQueryRequest) {
		//return userTaskHistoryService.getWorkflowNodes(workflowNodeQueryRequest);
		ListHistoricWorkflowNode listHistoricWorkflowNode = new ListHistoricWorkflowNode();
		List<HistoricWorkflowNode> data = userTaskHistoryService.getWorkflowNodes(workflowNodeQueryRequest);
		listHistoricWorkflowNode.setData(data);
		return listHistoricWorkflowNode;
	}

	@RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
	public HistoricWorkflowNode getHistoricActivity(@PathVariable String taskId){	
		HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		return userTaskHistoryService.getWorkflowNode(historicTaskInstance);		
	}
}
