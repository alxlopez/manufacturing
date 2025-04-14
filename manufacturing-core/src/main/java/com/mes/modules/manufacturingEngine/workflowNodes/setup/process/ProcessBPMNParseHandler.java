package com.mes.modules.manufacturingEngine.workflowNodes.setup.process;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.setup.util.ParseHandlerUtility;

@Component
public class ProcessBPMNParseHandler extends ProcessParseHandler {
	@Override
	protected void executeParse(BpmnParse bpmnParse, Process process) {
		initDefaultListeners(process);
		super.executeParse(bpmnParse, process);
	}

	private void initDefaultListeners(Process process) {		
		final List<FlowableListener> executionListeners = process.getExecutionListeners();
		FlowableListener processListenerStartEvent = ParseHandlerUtility
				.buildExecutionListener(new ProcessExecutionListenerStartEvent(), "start");
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(
				processListenerStartEvent
		);
		executionListeners.addAll(executionListners);
	}
}
