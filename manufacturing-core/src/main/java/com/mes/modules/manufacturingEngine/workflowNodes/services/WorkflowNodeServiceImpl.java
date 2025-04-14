package com.mes.modules.manufacturingEngine.workflowNodes.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.ServiceImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.event.Failure;
import com.mes.modules.manufacturingEngine.event.services.FailureRepository;
import com.mes.modules.manufacturingEngine.exception.WorkflowNodeExceptionCodes;
import com.mes.modules.manufacturingEngine.extensions.GetChildExecutionsCmd;
import com.mes.modules.manufacturingEngine.extensions.GetExecutionCmd;
import com.mes.modules.manufacturingEngine.extensions.GetSubProcessInstanceBySuperExecutionId;
import com.mes.modules.manufacturingEngine.extensions.SetParentExecutionCmd;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;

@Service("workflowNodeService")
public class WorkflowNodeServiceImpl extends ServiceImpl implements WorkflowNodeService {
	@Autowired
	private FailureRepository failureRepository;
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private ArtifactTimeService timeUtility;

	@Override
	public RuntimeService getEngineRuntimeService() {
		return beanFactory.getBean(RuntimeService.class);
	}

	@Override
	public Object getVariableLocal(String executionId, String variableName) {
		RuntimeService runtimeService = beanFactory.getBean(RuntimeService.class);
		return runtimeService.getVariableLocal(executionId, variableName);
	}

	@Override
	public void setVariableLocal(String executionId, String variableName, Object value) {
		RuntimeService runtimeService = beanFactory.getBean(RuntimeService.class);
		runtimeService.setVariableLocal(executionId, variableName, value);
	}

	@Override
	public Object getVariable(String executionId, String variableName) {		
		RuntimeService runtimeService = beanFactory.getBean(RuntimeService.class);
		return runtimeService.getVariableLocal(executionId, variableName);
	}

	@Override
	public void setVariables(String executionId, Map<String, ? extends Object> variables) {
		RuntimeService runtimeService = beanFactory.getBean(RuntimeService.class);
		runtimeService.setVariables(executionId, variables);
	}

	@Override
	public States getState(String workflowNodeId) {
		String state = (String) this.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_STATE);
		return States.valueOf(state);
	}

	@Override
	public void setState(String workflowNodeId, States state) {
		this.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_STATE, state.toString());
	}

	@Override
	public Long getFailuredId(String workflowNodeId) {
		Long failureId = (Long) this.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID);
		return failureId;
	}

	@Override
	public void setFailureId(String workflowNodeId, Long failureId) {
		this.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID, failureId);
	}

	@Override
	public String getRoutineId(String executionId) {
		return (String) this.getVariableLocal(executionId, BaseWorkflowNode.PROP_ROUTINE_ID);
	}

	@Override
	public void setRoutineId(String executionId, String routineId) {
		this.setVariableLocal(executionId, BaseWorkflowNode.PROP_ROUTINE_ID, routineId);
	}

	@Override
	public Artifact getArtifact(String executionId) {
		Artifact workflowNodeArtifact;
		String artifact = (String) this.getVariableLocal(executionId, BaseWorkflowNode.PROP_ARTIFACT);
		if (artifact == null) {
			return null;
		}
		String artifactLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, artifact.toString());				
		try {
			workflowNodeArtifact = beanFactory.getBean(artifactLowerCamel, Artifact.class);
		} catch (Exception e) {
			String workflowNodeType = (String) this.getVariableLocal(String.valueOf(executionId),
					BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE);
			throw new ManufacturingIllegalArgumentException("artifact not found with name " + artifactLowerCamel
					+ "set for workflowNodeType " + workflowNodeType + " with executionId: " + executionId);
		}		
		return workflowNodeArtifact;
	}

	@Override
	public void setArtifact(String executionId, ArtifactTypes artifact) {
		this.setVariableLocal(executionId, BaseWorkflowNode.PROP_ARTIFACT, artifact.toString());
	}

	@Override
	public void setWorkflowNodeType(String executionId, WorkflowNodeTypes workflowNodeType) {
		this.setVariableLocal(executionId, BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE, workflowNodeType.toString());
	}

	@Override
	public WorkflowNodeTypes getWorkflowNodeType(String executionId) {
		String workflowNodeTypeString = (String) this.getVariableLocal(executionId,
				BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE);
		if (workflowNodeTypeString == null) {
			return WorkflowNodeTypes.NOT_DEFINED;
		}
		return WorkflowNodeTypes.valueOf(workflowNodeTypeString);
	}

	@Override
	public ExecutionEntity getExecution(String executionId) {		
		return (ExecutionEntity) beanFactory.getBean(ManagementService.class)
				.executeCommand(new GetExecutionCmd(executionId));
	}

	@Override
	public String getProcessInstanceId(String workflowNodeId) {
		return this.getExecution(workflowNodeId).getProcessInstanceId();
	}

	@Override
	public List<ExecutionEntity> findChildExecutionsByParentExecutionId(String executionId) {
		ManagementService managementService = beanFactory.getBean(ManagementService.class);
		 return managementService.executeCommand(new GetChildExecutionsCmd(executionId));
	}

	public List<ExecutionEntity> findSubProcessesByProcessInstanceId(String executionId) {
		return null;
	}

	@Override
	public void validateNode(String executionId) {
		try {
			this.getVariableLocal(executionId, BaseWorkflowNode.PROP_STATE);
		} catch (Exception e) {
			throw new ManufacturingResourceNotFoundException("execution " + executionId + " doesn't exist");
		}
	}

	@Override
	public Long calculateEnlapsedTime(String workflowNodeId) {
		Date startTime = (Date) this.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		Date endTime = (Date) this.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME);
		Long enlapsedTime = timeUtility.getEnlapsedTime(startTime, endTime);
		return enlapsedTime;
	}

	@Override
	public Failure validateFailure(ExecutionEvent event) {
		EventParameter failureIdProperty = event.getParameter("failureId");
		if (failureIdProperty != null) {
			Long failureId = Long.valueOf(failureIdProperty.getValue());
			Failure failure = failureRepository.findById(failureId);
			if (failure == null) {
				throw new ManufacturingIllegalArgumentException(
						WorkflowNodeExceptionCodes.FailureIdUnKnown + ", failureId reported: " + failureId
				);
			}
			return failure;
		}
		return null;
	}

	@Override
	public WorkflowNode buildWorkflowNode(String executionId) {
		WorkflowNode workflowNode = new WorkflowNode();
		ExecutionEntity executionEntity = this.getExecution(executionId);
		workflowNode.setArtifactId(executionEntity.getId());
		workflowNode.setExecutionId(executionEntity.getId());
		workflowNode.setProcessInstanceId(executionEntity.getProcessInstanceId());
		workflowNode.setSuperExecutionId(executionEntity.getSuperExecutionId());
		workflowNode.setRootProcessInstanceId(executionEntity.getRootProcessInstanceId());
		workflowNode.setActivityId(executionEntity.getActivityId());		
		return workflowNode;
	}

	@Override
	public String findSuperExecutionId(String executionId) {
		ExecutionEntity executionEntity = this.getExecution(executionId);
		return executionEntity.getSuperExecutionId();
	}

	@Override
	public List<String> findChildExecutionIds(String ProcessExecutionId) {
		List<ExecutionEntity> childExecutions = this.findChildExecutionsByParentExecutionId(ProcessExecutionId);
		List<String> executionIds = new ArrayList<String>();
		for (ExecutionEntity execution : childExecutions) {
			executionIds.add(execution.getId());
		}
		return executionIds;
	}

	@Override
	public String findSubProcessInstanceIdBySuperExecutionId(String superExecutionId) {
		ManagementService managementService = beanFactory.getBean(ManagementService.class);	
		ExecutionEntity subProcessEntity = (ExecutionEntity) managementService.executeCommand(new GetSubProcessInstanceBySuperExecutionId(superExecutionId));
		if (subProcessEntity == null) {
			return null;
		}
		return subProcessEntity.getId();
	}

	@Override
	public Artifact getArtifactObject(ArtifactTypes artifactType) {		
		String artifactLowerCamel = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, artifactType.toString());
		Artifact workflowNodeArtifact = beanFactory.getBean(artifactLowerCamel, Artifact.class);
		return workflowNodeArtifact;
	}

	@Override
	public void setArtifactVariableLocal(String executionId, String variableName, Object value ) {
		Artifact artifact = this.getArtifact(executionId);
		WorkflowNode workflowNode = artifact.getArtifactService().buildWorkflowNodeByExecutionId(executionId);
		artifact.getArtifactService().setVariableLocal(workflowNode.getArtifactId(), variableName, value);		
	}

	@Override
	public void setParentExecution(String executionId, String parentExecutionId) {		
		ManagementService managementService = beanFactory.getBean(ManagementService.class);	
		managementService.executeCommand(new SetParentExecutionCmd(executionId, parentExecutionId));
	}
}
