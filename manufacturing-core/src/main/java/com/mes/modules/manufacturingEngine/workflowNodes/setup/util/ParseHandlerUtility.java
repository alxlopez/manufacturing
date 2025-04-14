package com.mes.modules.manufacturingEngine.workflowNodes.setup.util;

import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;

import com.google.common.base.CaseFormat;

public class ParseHandlerUtility {
	public static FlowableListener buildExecutionListener(ExecutionListener executionListener,String eventName){			
		FlowableListener listener = new FlowableListener();
		listener.setEvent(eventName);
		listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);				
		String listenerBeanName  =CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, executionListener.getClass().getSimpleName());		
		listener.setImplementation("${"+listenerBeanName+"}");
		return  listener;
	}

	public static FlowableListener buildTaskListener(TaskListener executionListener,String eventName){			
		FlowableListener listener = new FlowableListener();
		listener.setEvent(eventName);
		listener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);				
		String listenerBeanName  =CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, executionListener.getClass().getSimpleName());		
		listener.setImplementation("${"+listenerBeanName+"}");
		return listener;
	}	
}
