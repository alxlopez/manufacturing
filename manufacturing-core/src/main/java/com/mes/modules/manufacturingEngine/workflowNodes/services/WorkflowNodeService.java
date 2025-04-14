package com.mes.modules.manufacturingEngine.workflowNodes.services;

import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.Failure;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

public interface WorkflowNodeService {
	public RuntimeService getEngineRuntimeService();
	public WorkflowNode buildWorkflowNode(String executionId);
	public Object getVariableLocal(String executionId, String variableName);
	public Object getVariable(String executionId, String variableName);
	public Artifact getArtifact(String executionId);	
	public void setArtifactVariableLocal(String executionId, String variableName, Object value );
	public void setParentExecution(String executionId, String ParentExecutionId);
	public String findSuperExecutionId(String executionId);
	public void setWorkflowNodeType(String executionId, WorkflowNodeTypes workflowNodeType);
	public WorkflowNodeTypes getWorkflowNodeType(String executionId);
	public String getRoutineId(String executionId);
	public void setRoutineId(String executionId,String routineId);
	public void setArtifact(String executionId, ArtifactTypes artifact);
	public ExecutionEntity getExecution(String executionId);
	public States getState(String workflowNodeId);
	public Long getFailuredId(String workflowNodeId);
	public String getProcessInstanceId(String workflowNodeId);
	public void setVariableLocal(String executionId, String variableName, Object value);
	public void setVariables(String executionId, Map<String, ? extends Object> variables);
	public void setState(String workflowNodeId, States state);
	public void setFailureId(String workflowNodeId, Long failureId);
	public void validateNode(String executionId);
	public Failure validateFailure(ExecutionEvent event);
	public Long calculateEnlapsedTime(String workflowNodeId);
	public List<ExecutionEntity> findChildExecutionsByParentExecutionId(String executionId);
	public List<String> findChildExecutionIds(String ProcessExecutionId) ;
	public String findSubProcessInstanceIdBySuperExecutionId(String superExecutionId);
	public Artifact getArtifactObject(ArtifactTypes workflowNodeArtifactName);
}
