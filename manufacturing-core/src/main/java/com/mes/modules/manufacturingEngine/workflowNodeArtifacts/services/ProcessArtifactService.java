package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.enumerations.application.States;
import com.mes.modules.manufacturingEngine.extensions.ActivityStateReflectionService;
import com.mes.modules.manufacturingEngine.extensions.GetExecutionCmd;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Service
public class ProcessArtifactService implements ArtifactService {
	@Autowired
	private RuntimeService nodeService;
	@Autowired
	private ArtifactTimeService timeUtility;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private ActivityStateReflectionService activityStateReflectionService;
	@Autowired
	private BeanFactory beanFactory;

	@Override
	public String startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
		ProcessInstance processInstance = nodeService.startProcessInstanceByKey(processDefinitionKey, variables);
		return processInstance.getId();
	}
	
	@Override
	public void setVariableLocal(String workflowId, String variableName, Object value) {
		nodeService.setVariableLocal(workflowId, variableName, value);
	}

	@Override
	public Object getVariable(String workflowId, String variableName) {
		return nodeService.getVariable(workflowId, variableName);
	}

	@Override
	public Object getVariableLocal(String workflowId, String variableName) {
		return nodeService.getVariableLocal(workflowId, variableName);
	}

	public void validateArtifact(String processInstanceId) {
		try {
			// if execution not found, activiti throws exception
			nodeService.getVariableLocal(processInstanceId, BaseWorkflowNode.PROP_STATE);
		} catch (Exception e) {
			throw new ManufacturingResourceNotFoundException(
					"process instance " + processInstanceId + " doesn't exist");
		}
	}

	public void deleteProcessInstance(String processInstanceId, String deleteReason) {
		nodeService.deleteProcessInstance(processInstanceId, deleteReason);
	}

	@Override
	public void setState(String workflowNodeId, States state) {
		nodeService.setVariableLocal(workflowNodeId, "state", state.toString());
	}

	@Override
	public States getState(String workflowNodeId) {
		String state = (String) nodeService.getVariableLocal(workflowNodeId, "state");
		if (state == null) {
			return States.UNDEFINED;
		}
		return States.valueOf(state);
	}

	@Override
	public String getRoutineId(String workflowNodeId) {
		return (String) this.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_ROUTINE_ID);
	}

	@Override
	public void setRoutineId(String workflowNodeId, String routineId) {
		this.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_ROUTINE_ID, routineId);
	}

	@Override
	public String getProcessInstanceId(String workflowNodeId) {
		return workflowNodeId;
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
	public void setVariables(String workflowNodeId, Map<String, ? extends Object> variables) {
		nodeService.setVariables(workflowNodeId, variables);
	}

	@Override
	public Long calculateEnlapsedTime(String workflowNodeId) {
		Date startTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		Date endTime = (Date) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME);
		Long enlapsedTime = timeUtility.getEnlapsedTime(startTime, endTime);
		return enlapsedTime;
	}

	@Override
	public void setFailureId(String workflowNodeId, Long failureId) {
		nodeService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID, failureId);
	}

	@Override
	public Long getFailuredId(String workflowNodeId) {
		Long failureId = (Long) nodeService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID);
		return failureId;
	}

	@Override
	public WorkflowNode buildWorkflowNodeByExecutionId(String executionId) {
		WorkflowNode workflowNode = new WorkflowNode();
		ExecutionEntity executionEntity = this.getExecution(executionId);
		if (executionEntity == null) {
			throw new ManufacturingResourceNotFoundException("Process not found with executionId" + executionId);
		}
		workflowNode.setArtifactId(executionId);
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

	public List<Execution> findChildExecutionsByParentExecutionId(String executionId) {
		List<Execution> executions = nodeService.createExecutionQuery().parentId(executionId).list();
		return executions;
	}

	public Artifact getExecutionArtifact(String executionId) {
		String artifact = (String) this.getVariableLocal(executionId, BaseWorkflowNode.PROP_ARTIFACT);
		if (artifact == null) {
			return null;
		}
		String artifactLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, artifact.toString());
		Artifact workflowNodeArtifact = beanFactory.getBean(artifactLowerCamel, Artifact.class);
		return workflowNodeArtifact;
	}

	@Override
	public void reflectStateToRootProcess(WorkflowNode workflowNode,States state) {		
		if (workflowNode.getSuperExecutionId() != null) {
			WorkflowNode RootProcess = this.buildWorkflowNodeByExecutionId(workflowNode.getRootProcessInstanceId());
			activityStateReflectionService.reflect(RootProcess.getArtifactId(), state);
		}
	}
}
