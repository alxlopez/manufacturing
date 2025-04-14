package com.mes.modules.manufacturingEngine.workflowNodes.setup.subProcess;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.CallActivity;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.IOParameter;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.CallActivityParseHandler;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.setup.util.ParseHandlerUtility;

@Component
public class CallActivityBpmnParseHandler extends CallActivityParseHandler {
	@Override
	protected void executeParse(BpmnParse bpmnParse, CallActivity callActivity) {
		this.initDefaultInOutParameters(callActivity);
		this.initDefaultListeners(callActivity);
		super.executeParse(bpmnParse, callActivity);
	}

	private void initDefaultInOutParameters(CallActivity callActivity) {
		final List<IOParameter> activityOutParameters = callActivity.getOutParameters();
		List<IOParameter> outParameters = getDefaultOutParameters();
		activityOutParameters.addAll(outParameters);
		final List<IOParameter> activityInParameters = callActivity.getInParameters();
		List<IOParameter> inParameters = getDefaultOutParameters();
		activityInParameters.addAll(inParameters);
	}

	private List<IOParameter> getDefaultOutParameters() {
		final IOParameter inputParameter1 = new IOParameter();
		inputParameter1.setSourceExpression("${execution.getId()}");
		inputParameter1.setTarget("fromCallActivityExecutionId");
		final IOParameter inputParameter2 = new IOParameter();
		inputParameter2.setSourceExpression("${execution.getProcessInstanceId()}");
		inputParameter2.setTarget("fromCallProcessInstanceId");
		final List<IOParameter> defaultInParameters = Arrays.<IOParameter> asList(inputParameter1, inputParameter2);
		return defaultInParameters;
	}

	private void initDefaultListeners(CallActivity callActivity) {
		final List<FlowableListener> executionListeners = callActivity.getExecutionListeners();
		FlowableListener processListenerStartEvent = ParseHandlerUtility
				.buildExecutionListener(new CallActivityStartListener(), "start");
		FlowableListener processListenerEndEvent = ParseHandlerUtility
				.buildExecutionListener(new CallActivityEndListener(), "end");
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(processListenerStartEvent,
				processListenerEndEvent);
		executionListeners.addAll(executionListners);
	}
}
