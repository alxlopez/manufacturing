package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import java.util.List;

import lombok.Data;

@Data
public class ArtifactActionRequest {
	private String artifactId;
	private ArtifactActions action;
	private String issuerId;
	private List<ArtifactRestVariable> variables;
	private List<ArtifactRestVariable> transientVariables;

	public ArtifactRestVariable getVariable(String name) {
		for (ArtifactRestVariable ArtifactRestVariable: this.variables) {
			if (ArtifactRestVariable.getName().equals(name)) {
				return ArtifactRestVariable;
			}
		}
		return null;
	}
}
