package com.mes.modules.workDirective.services;

import java.util.Map;

import com.mes.dom.workDirective.WorkDirective;

public interface WorkDirectiveService {	
	public WorkDirective create(WorkDirective workDirective);
	public WorkDirective createBasedOnJobOrder(WorkDirective workDirective);
	public WorkDirective validateWorkDirective(Long workDirectiveId);
	public WorkDirective updateDescription(Long workDirectiveId,String description);
	public void validateReferencedDirectivesIdsParameter(WorkDirective workDirective);
	public void saveWorkDirectiveParameters(WorkDirective workDirective, Map<String, Object> variables); 
}
