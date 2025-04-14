package com.mes.modules.manufacturingEngine.workflowNodeArtifacts;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;

import lombok.Data;

@Data
public class ArtifactRestVariable {
	public enum RestVariableScope {LOCAL, GLOBAL};
	private String name;
	private String type;
	private RestVariableScope variableScope;
	private Object value;
	private String valueUrl;

	public String getScope() {
		String scope = null;
		if (variableScope != null) {
			scope = variableScope.name().toLowerCase();
		}
		return scope;
	}

	public void setScope(String scope) {
		setVariableScope(getScopeFromString(scope));
	}
	
	public static RestVariableScope getScopeFromString(String scope) {
		if (scope != null) {
			for (RestVariableScope s : RestVariableScope.values()) {
				if (s.name().equalsIgnoreCase(scope)) {
					return s;
				}
			}
			throw new FlowableIllegalArgumentException("Invalid variable scope: '" + scope + "'");
		}
		return null;
	}
}