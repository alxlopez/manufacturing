package com.mes.modules.manufacturingEngine.extensions;

import java.io.Serializable;
import java.util.List;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;

/**
 * @author Fabian vidal
 */
public class GetChildExecutionsCmd implements Command<List<ExecutionEntity>>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String executionId;

	public GetChildExecutionsCmd(String executionId) {
		this.executionId = executionId;
	}

	public List<ExecutionEntity> execute(CommandContext commandContext) {
		if (executionId == null) {
			throw new FlowableIllegalArgumentException("executionId is null");
		}
		List<ExecutionEntity> executions = CommandContextUtil
		.getExecutionEntityManager(commandContext)
		.findChildExecutionsByParentExecutionId(executionId);
		return executions;
	}
}