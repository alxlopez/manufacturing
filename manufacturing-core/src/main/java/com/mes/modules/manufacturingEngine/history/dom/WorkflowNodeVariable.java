package com.mes.modules.manufacturingEngine.history.dom;

import com.mes.dom.enumerations.application.ValueType;

import lombok.Data;

@Data
public class WorkflowNodeVariable {
	String name;
	Object value;
	ValueType type;
}
