package com.mes.modules.manufacturingEngine.workflowNodes.services;

public interface WorkflowNodeCommandService {
	public void cmdInitiate(String executionId);
	public void cmdStart(String executionId);
	public void cmdHold(String executionId,String failureId);
	public void cmdHold(String executionId);
	public void cmdRestart(String executionId);
	public void cmdAbort(String executionId);
	public void cmdStop(String executionId);
	public void cmdComplete(String executionId);
}
