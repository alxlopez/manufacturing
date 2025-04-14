package com.mes.modules.manufacturingEngine.extensions;

import java.io.Serializable;
import java.util.List;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 * @author Fabian vidal
 */
public class GetTaskCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String executionId;

	public GetTaskCmd(String executionId) {
		this.executionId = executionId;
	}

	public Object execute(CommandContext commandContext) {
		if (executionId == null) {
			throw new FlowableIllegalArgumentException("executionId is null");			
		}
		List<TaskEntity> tasks =  CommandContextUtil.getTaskService(commandContext).findTasksByExecutionId(executionId);		 
		return tasks.get(0);
	}
}
