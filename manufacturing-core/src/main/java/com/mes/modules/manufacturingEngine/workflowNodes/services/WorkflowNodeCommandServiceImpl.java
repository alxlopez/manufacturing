package com.mes.modules.manufacturingEngine.workflowNodes.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.dom.enumerations.application.Commands;
import com.mes.modules.manufacturingEngine.event.services.WorkflowNodeEventService;

@Service("workflowNodeCommandService")             
public class WorkflowNodeCommandServiceImpl implements WorkflowNodeCommandService {
	@Autowired
	private WorkflowNodeEventService eventService; 	

	@Override
	public void cmdInitiate(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.INITIATE);		
	}

	@Override
	public void cmdStart(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.START);		
	}

	@Override
	public void cmdHold(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.HOLD);		
	}

	@Override
	public void cmdHold(String executionId,String failureId) {
		eventService.throwEventByExecutionId(executionId,failureId, Commands.HOLD);		
	}	

	public void cmdRestart(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.RESTART);
	}

	@Override
	public void cmdAbort(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.ABORT);		
	}

	@Override
	public void cmdStop(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.STOP);		
	}

	@Override
	public void cmdComplete(String executionId) {
		eventService.throwEventByExecutionId(executionId, Commands.COMPLETE);		
	}
}
