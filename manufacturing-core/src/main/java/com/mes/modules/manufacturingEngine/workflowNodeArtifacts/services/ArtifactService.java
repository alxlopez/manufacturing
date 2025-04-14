package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;


import java.util.Map;

import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface ArtifactService {	
	public Object getVariableLocal(String artifactId, String variableName);
	public Object getVariable(String artifactId, String variableName);
	public void setVariableLocal(String artifactId, String variableName,Object value);
	public void setVariables(String artifactId,Map<String,? extends Object> variables);
	public String getRoutineId(String artifactId);
	public void setRoutineId(String artifactId,String routineId);		
	public String startProcessInstanceByKey(String processDefinition, Map<String, Object> variables);
	public void validateArtifact(String artifactId);
	public void setState(String artifactId, States state);	
	public void setFailureId(String artifactId,Long failureId);
	public Long getFailuredId(String artifactId);
	public States getState(String artifactId);
	public String getProcessInstanceId(String artifactId);
	public WorkflowNode buildWorkflowNodeByArtifactId(String artifactId);	
	public void adquire(String artifactId,String owner);
	public Long calculateEnlapsedTime(String artifactId);
	public WorkflowNode buildWorkflowNodeByExecutionId(String executionId) ;
	public void reflectStateToRootProcess(WorkflowNode workflowNode,States state); 
}
