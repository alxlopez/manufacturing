package com.mes.dom.workDirective;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.rest.core.config.Projection;

import com.mes.dom.enumerations.application.WorkTypes;
import com.mes.dom.workDirective.WorkDirective;
import com.mes.dom.workSchedule.JobOrder;

@Projection(name = "complete", types = WorkDirective.class)
public interface WithAllPropertiesProjection {
	public Long getId();
	public String getCode();
	public String getDescription();
	public WorkTypes getWorkType();
	public JobOrder getJobOrder();
	public WorkMaster getWorkMaster();
	public Collection<WorkDirectiveParameter> getParameters();
	public WorkDirective getParent();
	public Set<WorkDirective> getWorkDirectives();
}
