package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.modules.manufacturingEngine.extensions.GetExecutionCmd;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Component
public class SubprocessArtifactService implements ArtifactService {
	@Autowired
	private RuntimeService nodeService;	
	@Autowired
	private ArtifactTimeService timeUtility;
	@Autowired
	private ManagementService managementService;

	@Override
	public Object getVariableLocal(String workflowNodeId, String variableName) {
		return nodeService.getVariableLocal(this.getRoutineId(workflowNodeId), variableName);
	}

	@Override
	public Object getVariable(String workflowNodeId, String variableName) {
		return nodeService.getVariableLocal(this.getRoutineId(workflowNodeId), variableName);
	}

	@Override
	public void setVariableLocal(String workflowNodeId, String variableName, Object value) {
		nodeService.setVariableLocal(this.getRoutineId(workflowNodeId), variableName, value);
	}

	@Override
	public void setVariables(String workflowNodeId, Map<String, ? extends Object> variables) {
		nodeService.setVariables(this.getRoutineId(workflowNodeId), variables);
	}

	@Override
	public States getState(String workflowNodeId) {
		String subProcessInstanceId = this.getRoutineId(workflowNodeId);
		if (subProcessInstanceId == null) {
			return States.UNDEFINED;
		}
		String state = (String) nodeService.getVariableLocal(subProcessInstanceId, BaseWorkflowNode.PROP_STATE);
		if (state == null) {
			return States.UNDEFINED;
		}
		return States.valueOf(state);
	}

	@Override
	public void setState(String workflowNodeId, States state) {
		nodeService.setVariableLocal(this.getRoutineId(workflowNodeId), BaseWorkflowNode.PROP_STATE, state.toString());
	}	

	@Override
	public String startProcessInstanceByKey(String processDefinition, Map<String, Object> variables) {
		return null;
	}

	@Override
	public void validateArtifact(String workflowNodeId) {
		try {
			// if execution not found, activiti throws exception
			nodeService.getVariableLocal(this.getRoutineId(workflowNodeId), BaseWorkflowNode.PROP_STATE);
		} catch (Exception e) {
			throw new ManufacturingResourceNotFoundException("SubProcess  " + workflowNodeId + " doesn't exist");
		}
	}

	@Override
	public void setFailureId(String workflowNodeId, Long failureId) {
		nodeService.setVariableLocal(this.getRoutineId(workflowNodeId), BaseWorkflowNode.PROP_FAILURE_ID, failureId);
	}

	@Override
	public Long getFailuredId(String workflowNodeId) {
		Long failureId = (Long) nodeService.getVariableLocal(this.getRoutineId(workflowNodeId),
				BaseWorkflowNode.PROP_FAILURE_ID);
		return failureId;
	}

	@Override
	public String getProcessInstanceId(String workflowNodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowNode buildWorkflowNodeByArtifactId(String artifactId) {		
		return buildWorkflowNodeByExecutionId(artifactId);
	}

	@Override
	public void adquire(String workflowNodeId, String owner) {
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_OWNER_ID, owner);
	}

	@Override
	public Long calculateEnlapsedTime(String workflowNodeId) {
		Date startTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		Date endTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME);
		Long enlapsedTime = timeUtility.getEnlapsedTime(startTime, endTime);
		return enlapsedTime;
	}

	@Override
	public String getRoutineId(String workflowNodeId) {
		return (String) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_ROUTINE_ID);
	}

	@Override
	public void setRoutineId(String workflowNodeId, String routineId) {
		this.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_ROUTINE_ID, routineId);

	}

	public List<String> findChildExecutionIds(String workflowNodeId) {
		List<Execution> childExecutions = nodeService.createExecutionQuery().parentId(workflowNodeId).list();
		List<String> executionIds = new ArrayList<String>();
		for (Execution execution : childExecutions) {
			executionIds.add(execution.getId());
		}
		return executionIds;
	}

	public void setWorkflowNodeType(String executionId, WorkflowNodeTypes workflowNodeType) {
		this.setVariableLocal(executionId, BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE, workflowNodeType.toString());
	}

	public WorkflowNodeTypes getWorkflowNodeType(String executionId) {
		String workflowNodeTypeString = (String) nodeService.getVariableLocal(executionId,
				BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE);
		if (workflowNodeTypeString == null) {
			return WorkflowNodeTypes.NOT_DEFINED;
		}
		return WorkflowNodeTypes.valueOf(workflowNodeTypeString);
	}

	@Override
	public WorkflowNode buildWorkflowNodeByExecutionId(String executionId) {
		WorkflowNode workflowNode = new WorkflowNode();
		ExecutionEntity executionEntity = this.getExecution(executionId);
		if (executionEntity == null) {
			throw new ManufacturingResourceNotFoundException("SubProcess(callActivity) not found with executionId" + executionId);
		}		
		workflowNode.setArtifactId(executionEntity.getId());
		workflowNode.setExecutionId(executionEntity.getId());
		workflowNode.setProcessInstanceId(executionEntity.getProcessInstanceId());
		workflowNode.setSuperExecutionId(executionEntity.getSuperExecutionId());
		workflowNode.setRootProcessInstanceId(executionEntity.getRootProcessInstanceId());
		workflowNode.setActivityId(executionEntity.getActivityId());
		return workflowNode;
	}

	public ExecutionEntity getExecution(String artifactId) {
		return (ExecutionEntity) managementService.executeCommand(new GetExecutionCmd(artifactId));
	}

	@Override
	public void reflectStateToRootProcess(WorkflowNode workflowNode, States state) { }
}
