package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.ServiceImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.config.exception.ManufacturingResourceNotFoundException;
import com.mes.dom.enumerations.application.States;
import com.mes.dom.event.EventParameter;
import com.mes.dom.event.ExecutionEvent;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.modules.manufacturingEngine.extensions.ActivityStateReflectionService;
import com.mes.modules.manufacturingEngine.extensions.GetExecutionCmd;
import com.mes.modules.manufacturingEngine.extensions.GetTaskCmd;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactRestVariable;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactRestVariable.RestVariableScope;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactTimeService;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.WorkflowNode;
import com.mes.modules.workDirective.services.WorkDirectiveService;

@Service
public class UserTaskArtifactService extends ServiceImpl implements ArtifactService {
	@Autowired
	private ArtifactTimeService artifactTimeUtility;
	@Autowired
	private WorkDirectiveService workDirectiveService;
	@Autowired
	private ActivityStateReflectionService activityStateReflectionService;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private TaskService coreElementService;

	public TaskService getCoreElementService() {
		return coreElementService;		
	}

	@Override
	public String startProcessInstanceByKey(String processDefinition, Map<String, Object> variables) {
		RuntimeService runtimeService = beanFactory.getBean(RuntimeService.class);
		return runtimeService.startProcessInstanceByKey(processDefinition, variables).getId();
	}

	@Override
	public void setVariables(String workflowNodeId, Map<String, ? extends Object> variables) {
		coreElementService.setVariables(workflowNodeId, variables);
	}

	@Override
	public void setVariableLocal(String taskId, String variableName, Object value) {
		coreElementService.setVariableLocal(taskId, variableName, value);
	}

	@Override
	public Object getVariable(String taskId, String variableName) {
		return coreElementService.getVariable(taskId, variableName);
	}

	@Override
	public Object getVariableLocal(String taskId, String variableName) {
		return coreElementService.getVariableLocal(taskId, variableName);
	}

	@Override
	public void validateArtifact(String nodeId) {
		try {
			coreElementService.getVariableLocal(nodeId, BaseWorkflowNode.PROP_STATE);
		} catch (Exception e) {
			throw new ManufacturingResourceNotFoundException("user Task " + nodeId + " doesn't exist");
		}
	}

	public List<Task> getWorkflowTasks(String workflowId) {
		return coreElementService.createTaskQuery().processInstanceId(workflowId).list();
	}

	@Override
	public void setState(String workflowNodeId, States state) {
		coreElementService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_STATE, state.toString());
	}

	@Override
	public States getState(String workflowNodeId) {
		String state = (String) coreElementService.getVariableLocal(workflowNodeId, "state");
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
		Task task = coreElementService.createTaskQuery().taskId(workflowNodeId).singleResult();
		return task.getProcessInstanceId();
	}

	public Boolean validateTasksAtSameState(String workflowId, States state) {
		List<Task> tasks = coreElementService.createTaskQuery().executionId(workflowId).list();
		for (Task task : tasks) {
			States taskCurrentState = this.getState(task.getId());
			if ((taskCurrentState != state)) {
				return false;
			}
		}
		return true;
	}

	public String GetAssignee(String UserTaskId) {
		Task task = coreElementService.createTaskQuery().taskId(UserTaskId).singleResult();
		return task.getAssignee();
	}

	@Override
	public WorkflowNode buildWorkflowNodeByArtifactId(String workflowNodeId) {
		WorkflowNode workflowNode = new WorkflowNode();
		Task task = this.getTaskById(workflowNodeId);
		workflowNode.setArtifactId(task.getId());
		ExecutionEntity executionEntity = this.getExecution(task.getExecutionId());
		workflowNode.setExecutionId(executionEntity.getId());
		workflowNode.setProcessInstanceId(executionEntity.getProcessInstanceId());
		workflowNode.setSuperExecutionId(executionEntity.getSuperExecutionId());
		workflowNode.setRootProcessInstanceId(executionEntity.getRootProcessInstanceId());
		workflowNode.setActivityId(executionEntity.getActivityId());
		return workflowNode;
	}

	public void claim(String taskId, String assignee) {
		coreElementService.claim(taskId, assignee);
	}

	public void complete(String workflowNodeId, Map<String, Object> variables) {
		coreElementService.complete(workflowNodeId, variables);
	}

	@Override
	public void adquire(String workflowNodeId, String owner) {
		coreElementService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_OWNER_ID, owner);
	}

	public Map<String, Object> BuildVariablesMap(ExecutionEvent event) {
		Collection<EventParameter> parameters = event.getParameters();
		Map<String, Object> variables = new HashMap<String, Object>();
		for (EventParameter eventParameter : parameters) {
			String valueString = eventParameter.getValue();
			if (eventParameter.getValueType() != null) {
				String valueType = eventParameter.getValueType().toString().toLowerCase();
				Object valueObject = this.getObjectVariable(valueString, valueType);
				variables.put(eventParameter.getCode(), valueObject);
			} else {
				variables.put(eventParameter.getCode(), valueString);
			}
		}
		return variables;
	}

	public void upsertVariablesBasedOnScope(String artifactId, List<ArtifactRestVariable> variables) {
		if (variables != null) {
			for (ArtifactRestVariable variable : variables) {
				RestVariableScope variableScope = ArtifactRestVariable.getScopeFromString(variable.getScope());
				if (variableScope == RestVariableScope.LOCAL) {
					coreElementService.setVariableLocal(artifactId, variable.getName(), variable.getValue());
				} else if (variableScope == RestVariableScope.GLOBAL) {
					coreElementService.setVariable(artifactId, variable.getName(), variable.getValue());
				}
			}
		}
	}

	private Object getObjectVariable(String value, String valueType) {
		switch (valueType) {
		case "float":
			return Float.parseFloat(value);
		case "integer":
			return Integer.parseInt(value);
		case "string":
			return value;
		case "boolean":
			return Boolean.parseBoolean(value);
		default:
			throw new ManufacturingIllegalArgumentException("valueType specified is not supported");
		}
	}

	@Override
	public Long calculateEnlapsedTime(String workflowNodeId) {
		Date startTime = (Date) coreElementService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_START_TIME);
		Date endTime = (Date) coreElementService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_END_TIME);
		Long enlapsedTime = artifactTimeUtility.getEnlapsedTime(startTime, endTime);
		return enlapsedTime;
	}

	public void setExecutionHeldTime(String workflowNodeId, Long HeldDuration) {
		Long heldTime = (long) coreElementService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_HELD_TIME);
		if (heldTime.equals(null)) {
			coreElementService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_HELD_TIME, HeldDuration);
		} else {
			coreElementService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_HELD_TIME, heldTime + HeldDuration);
		}
	}

	@Override
	public void setFailureId(String workflowNodeId, Long failureId) {
		coreElementService.setVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID, failureId);
	}

	@Override
	public Long getFailuredId(String workflowNodeId) {
		Long failureId = (Long) coreElementService.getVariableLocal(workflowNodeId, BaseWorkflowNode.PROP_FAILURE_ID);
		return failureId;
	}

	@Override
	public WorkflowNode buildWorkflowNodeByExecutionId(String executionId) {
		WorkflowNode workflowNode = new WorkflowNode();
		TaskEntity task = findTaskByExecutionId(executionId);
		if (task == null) {
			throw new ManufacturingResourceNotFoundException("User Task not found with executionId:" + executionId);
		}
		workflowNode.setArtifactId(task.getId());
		ExecutionEntity executionEntity = this.getExecution(task.getExecutionId());
		workflowNode.setExecutionId(executionEntity.getId());
		workflowNode.setProcessInstanceId(executionEntity.getProcessInstanceId());
		workflowNode.setSuperExecutionId(executionEntity.getSuperExecutionId());
		workflowNode.setRootProcessInstanceId(executionEntity.getRootProcessInstanceId());
		workflowNode.setActivityId(executionEntity.getActivityId());
		return workflowNode;
	}

	public TaskEntity findTaskByExecutionId(String executionId) {
		TaskEntity taskEntity = (TaskEntity) managementService.executeCommand(new GetTaskCmd(executionId));		
		return taskEntity;
	}

	public ExecutionEntity getExecution(String executionId) {
		return (ExecutionEntity) managementService.executeCommand(new GetExecutionCmd(executionId));
	}

	public Task getTaskById(String taskId) {
		Task task = coreElementService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	public void saveWorkDirectiveParameters(WorkDirective workDirective, Map<String, Object> variables){		
		workDirectiveService.saveWorkDirectiveParameters(workDirective, variables);
	} 

	@Override
	public void reflectStateToRootProcess(WorkflowNode workflowNode, States state) {
		activityStateReflectionService.reflect(workflowNode.getProcessInstanceId(), state);
	}
}
