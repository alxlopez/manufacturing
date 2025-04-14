package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import com.mes.dom.enumerations.application.ArtifactVariableScope;

import lombok.Data;

@Data
public class ArtifactVariable {
	private String name;
	private String type;
	private ArtifactVariableScope scope;
	private Object value;
}
