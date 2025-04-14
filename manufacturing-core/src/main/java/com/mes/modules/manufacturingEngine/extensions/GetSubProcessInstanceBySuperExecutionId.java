package com.mes.modules.manufacturingEngine.extensions;

import java.io.Serializable;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;

/**
 * @author Fabian vidal
 */
public class GetSubProcessInstanceBySuperExecutionId implements Command<Object>, Serializable {
	private static final long serialVersionUID = 1L;
	protected String superExecutionId;

	public GetSubProcessInstanceBySuperExecutionId(String superExecutionId) {
		this.superExecutionId = superExecutionId;
	}

	public Object execute(CommandContext commandContext) {
		if (superExecutionId == null) {
			throw new FlowableIllegalArgumentException("superExecutionId is null");
		}
		ExecutionEntity execution = CommandContextUtil.getExecutionEntityManager(commandContext).findSubProcessInstanceBySuperExecutionId(superExecutionId);
		return execution;
	}
}