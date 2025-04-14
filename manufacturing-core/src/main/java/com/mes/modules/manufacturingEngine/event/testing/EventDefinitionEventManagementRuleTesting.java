package com.mes.modules.manufacturingEngine.event.testing;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.mes.dom.event.EventDefinition;
import com.mes.dom.event.ExecutionEvent;

public class EventDefinitionEventManagementRuleTesting {	
	public static final void main(String[] args) {
		try {
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-eventDefinitionEventManagement");
			ExecutionEvent event = new ExecutionEvent();
			EventDefinition eventDefinition = new  EventDefinition();
			event.setEventDefinition(eventDefinition);
			kSession.insert(event);
			kSession.fireAllRules();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
