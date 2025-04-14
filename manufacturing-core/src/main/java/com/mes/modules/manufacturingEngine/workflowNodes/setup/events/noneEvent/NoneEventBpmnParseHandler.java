package com.mes.modules.manufacturingEngine.workflowNodes.setup.events.noneEvent;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.EventDefinition;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ThrowEvent;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.IntermediateThrowEventParseHandler;
import org.springframework.stereotype.Component;

@Component
public class NoneEventBpmnParseHandler extends IntermediateThrowEventParseHandler {	
	protected void executeParse(BpmnParse bpmnParse, ThrowEvent intermediateEvent) {
		EventDefinition eventDefinition = null;
		if (!intermediateEvent.getEventDefinitions().isEmpty()) {
			eventDefinition = intermediateEvent.getEventDefinitions().get(0);
		}
		if (eventDefinition == null) {
			initDefaultListeners(intermediateEvent);
		}
		super.executeParse(bpmnParse, intermediateEvent);
	}

	private void initDefaultListeners(ThrowEvent intermediateEvent) {
		final List<FlowableListener> executionListeners = intermediateEvent.getExecutionListeners();
		FlowableListener processListenerStartEvent = new FlowableListener();		
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(processListenerStartEvent);
		executionListeners.addAll(executionListners);
	}
}
