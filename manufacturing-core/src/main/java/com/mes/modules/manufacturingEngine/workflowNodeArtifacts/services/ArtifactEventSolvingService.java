package com.mes.modules.manufacturingEngine.workflowNodeArtifacts.services;

import com.mes.dom.enumerations.application.ArtifactTypes;

public interface ArtifactEventSolvingService {	
	public String createEventSolvingProcess(String artifactId,ArtifactTypes artifactType, Long failureId, String reporterId,String comment);
	public void setProcessAsChildExecution(String eventSolvingProcessId,String parentExecutionId);
}
