package com.mes.modules.manufacturingEngine.extensions;

import java.io.Serializable;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.runtime.Execution;

/**
 * @author Fabian vidal
 */
public class GetExecutionCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String executionId;

	public GetExecutionCmd(String executionId) {
		this.executionId = executionId;
	}

	public Object execute(CommandContext commandContext) {
		if (executionId == null) {
			throw new FlowableIllegalArgumentException("executionId is null");
		}
		ExecutionEntity execution = CommandContextUtil.getExecutionEntityManager(commandContext).findById(executionId);
		if (execution == null) {
			throw new FlowableObjectNotFoundException("execution " + executionId + " doesn't exist", Execution.class);
		}
		return execution;
	}
}
