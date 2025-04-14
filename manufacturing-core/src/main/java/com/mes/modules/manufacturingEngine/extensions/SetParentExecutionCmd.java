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
public class SetParentExecutionCmd implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String parentExecutionId;
	protected String executionId;

	public SetParentExecutionCmd( String executionId,String parentExecutionId) {
		this.executionId = executionId;
		this.parentExecutionId = parentExecutionId;
	}

	public Object execute(CommandContext commandContext) {
		if (parentExecutionId == null) {
			throw new FlowableIllegalArgumentException("executionId is null");
		}
		ExecutionEntity parentExecutionEntity = CommandContextUtil.getExecutionEntityManager(commandContext)
		.findById(parentExecutionId);
		if (parentExecutionEntity == null) {
			throw new FlowableObjectNotFoundException("execution " + parentExecutionId + " doesn't exist", Execution.class);
		}
		ExecutionEntity childExecutionEntity = CommandContextUtil.getExecutionEntityManager(commandContext).findById(executionId);
		if (childExecutionEntity == null) {
			throw new FlowableObjectNotFoundException("childExecution " + executionId + " doesn't exist", Execution.class);
		}
		childExecutionEntity.setParentId(parentExecutionEntity.getId());
		CommandContextUtil.getExecutionEntityManager(commandContext).update(childExecutionEntity);
		return parentExecutionEntity;
	}
}
