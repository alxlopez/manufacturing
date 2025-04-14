package com.mes.config.processEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManagerFactory;

import org.flowable.engine.common.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.parse.BpmnParseHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import com.mes.modules.manufacturingEngine.extensions.CustomMybatisMapper;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.ArtifactStateChangedListener;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.events.EndEvent.EndEventBpmnParseHandler;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.events.noneEvent.NoneEventBpmnParseHandler;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.events.startEvent.StartEventBpmnParseHandler;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.process.ProcessBPMNParseHandler;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.process.ProcessStateChangedListener;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.subProcess.CallActivityBpmnParseHandler;
import com.mes.modules.manufacturingEngine.workflowNodes.setup.userTasks.UserTaskBpmnParseHandler;

@Configuration
@AutoConfigureAfter(org.flowable.spring.boot.AbstractProcessEngineAutoConfiguration.class)
public class ExtraProcessEngineConfig implements ProcessEngineConfigurationConfigurer {
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private ArtifactStateChangedListener  artifactStateChangedListener;
	@Autowired
	private ProcessStateChangedListener  processStateChangedListener;

	public void configure(SpringProcessEngineConfiguration engineConfiguration) {
		this.setCustomDefaultBpmnParseHandlers(engineConfiguration);		
		this.setTypedEventListerners(engineConfiguration);
		this.setCutomMybatisMappers(engineConfiguration);
	}

	private void setCustomDefaultBpmnParseHandlers(SpringProcessEngineConfiguration engineConfiguration) {
		List<BpmnParseHandler> customDefaultBpmnParseHandlers = Arrays.<BpmnParseHandler> asList(
				new ProcessBPMNParseHandler(),
				new StartEventBpmnParseHandler(),
				new EndEventBpmnParseHandler(),
				new UserTaskBpmnParseHandler(),
				new CallActivityBpmnParseHandler(),
				new NoneEventBpmnParseHandler()
		);
		engineConfiguration.setCustomDefaultBpmnParseHandlers(customDefaultBpmnParseHandlers)
		.setJpaEntityManagerFactory(entityManagerFactory)
		.setJpaHandleTransaction(true)
		.setJpaCloseEntityManager(true);
	}

	private void setTypedEventListerners(SpringProcessEngineConfiguration engineConfiguration) {
		Map<String, List<FlowableEventListener>> typedEventListeners = new  HashMap<String, List<FlowableEventListener>>();
        this.putVariableUpsertEventListeners(typedEventListeners); 
        this.putCustomEventListerners(typedEventListeners);
        engineConfiguration.setTypedEventListeners(typedEventListeners);
	}

	private void putVariableUpsertEventListeners(Map<String, List<FlowableEventListener>> typedEventListeners){
		List<FlowableEventListener> variableEventListeners = Arrays
		.<FlowableEventListener> asList(
				processStateChangedListener,
				artifactStateChangedListener
		);
		typedEventListeners.put(FlowableEngineEventType.VARIABLE_CREATED.toString(), variableEventListeners);
		typedEventListeners.put(FlowableEngineEventType.VARIABLE_UPDATED.toString(), variableEventListeners);		
	}	

	private void putCustomEventListerners(Map<String, List<FlowableEventListener>> typedEventListeners) {
		List<FlowableEventListener> customEventListeners = Arrays.<FlowableEventListener> asList(
				//
		);
		typedEventListeners.put("CUSTOM", customEventListeners);
	}

	private void setCutomMybatisMappers(SpringProcessEngineConfiguration engineConfiguration){
		Set<Class<?>> customMybatisMappers = new HashSet<Class<?>>();
		customMybatisMappers.add(CustomMybatisMapper.class);
		engineConfiguration.setCustomMybatisMappers(customMybatisMappers);
	}
}
