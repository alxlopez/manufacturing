package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.artifacts.process.subTypes.processOrchestator.services;

import java.util.List;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.modules.manufacturingEngine.workflowNodes.BaseWorkflowNode;
import com.mes.modules.manufacturingEngine.workflowNodes.services.WorkflowNodeService;

@Service("orchestratorProcessService")
public class OrchestratorProcessServiceImpl implements OrchestratorProcessService{
	@Autowired
	private WorkflowNodeService workflowNodeService;

	@Override
	public void clearOwnedWorkflowSpecifications(String ownerWorkflowSpecificationId) {
		List<ProcessInstance> ownedProcessInstances = workflowNodeService.getEngineRuntimeService()
		.createProcessInstanceQuery()
		.variableValueEquals(BaseWorkflowNode.PROP_OWNER_ID, ownerWorkflowSpecificationId).list();
		for (ProcessInstance processInstance : ownedProcessInstances) {
			workflowNodeService.getEngineRuntimeService().setVariableLocal(processInstance.getId(),
					BaseWorkflowNode.PROP_OWNER_ID, null);
		}
	}
}
