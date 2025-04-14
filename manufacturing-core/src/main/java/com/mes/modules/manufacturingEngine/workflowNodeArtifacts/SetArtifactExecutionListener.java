package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.CaseFormat;
import com.mes.config.exception.ManufacturingIllegalArgumentException;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
@Scope("prototype")
public class SetArtifactExecutionListener implements ExecutionListener {
	private static final long serialVersionUID = 2159845417470649931L;
	@Autowired
	private BeanFactory beanFactory;	
	private Expression artifact;

	@Override
	public void notify(DelegateExecution execution) {
		if (artifact != null) {
			String artifactString = (String) artifact.getValue(execution);			
			try {
				beanFactory.getBean(artifactString);
			} catch (Exception e) {				
				throw new ManufacturingIllegalArgumentException(
						"artifact not found with name " + artifactString +
						" set at executionListener"
				);
			}
			String artifactUpperUnderScore = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, artifactString);
			execution.setVariableLocal(BaseWorkflowNode.PROP_ARTIFACT, artifactUpperUnderScore);
		}
	}
}
