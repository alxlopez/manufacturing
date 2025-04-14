package com.mes.modules.manufacturingEngine.workflowNodes.setup.events.startEvent;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StartEventExecutionListenerStartEvent implements ExecutionListener {
	private static final long serialVersionUID = -1052662458234389116L;	

	@Override
	public void notify(DelegateExecution execution) { }
}
