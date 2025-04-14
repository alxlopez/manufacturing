package com.mes.modules.manufacturingEngine.history.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mes.modules.manufacturingEngine.history.dom.HistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.ListHistoricWorkflowNode;
import com.mes.modules.manufacturingEngine.history.dom.WorkflowNodeQueryRequest;
import com.mes.modules.manufacturingEngine.history.service.ProcessHistoryService;

@RestController
@RequestMapping("/mes/history/processes")
public class ProcessHistoryController {
	@Autowired
	private ProcessHistoryService processHistoryService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ListHistoricWorkflowNode getWorkflowNodes(@RequestBody WorkflowNodeQueryRequest workflowNodeQueryRequest) {
		ListHistoricWorkflowNode listHistoricWorkflowNode = new ListHistoricWorkflowNode();
		List<HistoricWorkflowNode> data = processHistoryService.getHistoricProcesses(workflowNodeQueryRequest);
		listHistoricWorkflowNode.setData(data);
		return listHistoricWorkflowNode;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HistoricWorkflowNode getworkflowNode(@PathVariable String id){		
		return processHistoryService.getHistoricProcess(id);		
	}

	@RequestMapping(value = "/{id}/journal", method = RequestMethod.GET)
	public HistoricWorkflowNode getProcessJournal(@PathVariable String id){		
		HistoricWorkflowNode historicWorkflowNode= processHistoryService.getHistoricProcess(id);
		List<HistoricWorkflowNode> userTasks =processHistoryService.getProcessHistoricUserTasks(id);
		historicWorkflowNode.setActivities(userTasks);		
		return historicWorkflowNode;	
	}
}
