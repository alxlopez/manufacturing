package com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks;

import java.util.Arrays;
import java.util.List;

import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.springframework.stereotype.Component;

import com.mes.modules.manufacturingEngine.workflowNodes.setup.util.ParseHandlerUtility;

@Component
public class UserTaskBpmnParseHandler extends UserTaskParseHandler {
	@Override
	protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
		this.initDefaultExecutionListeners(userTask);
		this.initDefaultTaskListeners(userTask);
		super.executeParse(bpmnParse, userTask);
	}

	private void initDefaultExecutionListeners(UserTask userTask) {
		final List<FlowableListener> executionListeners = userTask.getExecutionListeners();
		FlowableListener startEventListener = ParseHandlerUtility
				.buildExecutionListener(new UserTaskExecutionListenerStartEvent(), "start");
		FlowableListener endEventListener = ParseHandlerUtility
				.buildExecutionListener(new UserTaskExecutionListenerEndEvent(), "end");
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(
				startEventListener,
				endEventListener);
		executionListeners.addAll(executionListners);
	}

	private void initDefaultTaskListeners(UserTask userTask) {
		final List<FlowableListener> taskListeners = userTask.getTaskListeners();
		FlowableListener taskListenerStartEvent = ParseHandlerUtility
				.buildTaskListener(new UserTaskListenerCreateEvent(), TaskListener.EVENTNAME_CREATE);
		FlowableListener taskListenerAssignmentEvent = ParseHandlerUtility
				.buildTaskListener(new UserTaskListenerAssigmentEvent(), TaskListener.EVENTNAME_ASSIGNMENT);
		FlowableListener taskListenerCompleteEvent = ParseHandlerUtility
				.buildTaskListener(new UserTaskListenerCompleteEvent(), TaskListener.EVENTNAME_COMPLETE);
		FlowableListener taskListenerDeleteEvent = ParseHandlerUtility
				.buildTaskListener(new UserTaskListenerDeleteEvent(), TaskListener.EVENTNAME_DELETE);
		final List<FlowableListener> executionListners = Arrays.<FlowableListener> asList(
				taskListenerStartEvent,
				taskListenerAssignmentEvent,
				taskListenerCompleteEvent,
				taskListenerDeleteEvent);
		taskListeners.addAll(executionListners);
	}
}
