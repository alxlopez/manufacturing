package com.mes.modules.manufacturingEngine.workflowNodes.setup.events.EndEvent;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.EndEventParseHandler;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.setup.util.ParseHandlerUtility;

@Component
public class EndEventBpmnParseHandler extends EndEventParseHandler {
	@Override
	protected void executeParse(BpmnParse bpmnParse, EndEvent endEvent) {
		this.initDefaultListeners(endEvent);
		super.executeParse(bpmnParse, endEvent);
	}

	private void initDefaultListeners(EndEvent endEvent) {
		final List<FlowableListener> executionListeners = endEvent.getExecutionListeners();
		FlowableListener processListenerStartEvent = ParseHandlerUtility
				.buildExecutionListener(new CompleteProcessExecutionListener(), "start");
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(processListenerStartEvent);
		executionListeners.addAll(executionListners);
	}
}
