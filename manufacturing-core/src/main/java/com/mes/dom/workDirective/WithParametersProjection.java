package com.mes.dom.workDirective;

import java.util.Collection;

import org.springframework.data.rest.core.config.Projection;

import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.workDirective.WorkDirective;

@Projection(name = "parameters", types = WorkDirective.class)
public interface WithParametersProjection {
	public Long getId();
	public String getCode();
	public String getDescription();
	public WorkTypes getWorkType();
	public Collection<WorkDirectiveParameter> getParameters();
}
