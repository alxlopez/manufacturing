package com.mes.modules.manufacturingEngine.workflowNodes.setup.subProcess;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;

@Component
public class CallActivityStartListener implements ExecutionListener {
	private static final long serialVersionUID = -2153410674392880282L;

	@Override
	public void notify(DelegateExecution execution) {
		this.setImplementationType(execution);		
	}

	private void setImplementationType(DelegateExecution execution) {
		execution.setVariableLocal(BaseWorkflowNode.PROP_WORKFLOWNODE_TYPE, WorkflowNodeTypes.SUBPROCESS.toString());
		execution.setVariableLocal("artifact", ArtifactTypes.SUBPROCESS.toString());		
	}
}
