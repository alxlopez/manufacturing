package com.mes.modules.milanes.testing;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.mes.modules.milanes.fact.ToProcess;

public class RuleTesting {
	public static final void main(String[] args) {
		try {
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			KieSession kSession = kContainer.newKieSession("ksession-rulesDays");
			ToProcess toProcess = new ToProcess();		
			toProcess.setWorkType("AT");
			kSession.insert(toProcess);
			kSession.fireAllRules();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
