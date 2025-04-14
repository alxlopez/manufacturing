package com.mes.modules.manufacturingEngine.workflowNodes;

import com.mes.dom.enumerations.application.States;
import com.mes.dom.enumerations.application.ArtifactTypes;
import com.mes.dom.enumerations.application.WorkflowNodeTypes;
import com.mes.modules.manufacturingEngine.workflowNodeArtifacts.Artifact;

import lombok.Data;

@Data
public class WorkflowNode {	
	private String artifactId;
	private String executionId;
	private String processInstanceId;
	private String superExecutionId;
	private String rootProcessInstanceId;
	private String activityId;
	private WorkflowNodeTypes type;
	private States state;
	private ArtifactTypes artifactType;
	private Artifact artifact;
}
