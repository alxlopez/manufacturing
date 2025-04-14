package com.mes.modules.manufacturingEngine.workflowNodes.setup.events.startEvent;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.StartEventParseHandler;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.setup.util.ParseHandlerUtility;

@Component
public class StartEventBpmnParseHandler extends StartEventParseHandler {
	@Override
	protected void executeParse(BpmnParse bpmnParse, StartEvent element) {
		super.executeParse(bpmnParse, element);
	}

	public void initDefaultListeners(StartEvent element) {
		final List<FlowableListener> executionListeners = element.getExecutionListeners();
		FlowableListener processListenerStartEvent = ParseHandlerUtility
				.buildExecutionListener(new StartEventExecutionListenerStartEvent(), "start");
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(processListenerStartEvent);
		executionListeners.addAll(executionListners);
	}
}