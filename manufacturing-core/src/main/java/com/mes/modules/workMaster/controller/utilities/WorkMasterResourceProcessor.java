package com.mes.modules.workMaster.controller.utilities;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkMaster;

@Component
public class WorkMasterResourceProcessor implements ResourceProcessor<Resource<WorkMaster>> {
	@Override
	public Resource<WorkMaster> process(Resource<WorkMaster> resource) {
		WorkMaster workMaster = resource.getContent();
		if (workMaster.getWorkflowSpecificationId() != null) {
//			resource.add(linkTo(methodOn(ProcessInstanceResource.class).getProcessInstance(workDirective.getWorkflowSpecificationId(), null)).withRel("workflowSpecification"));
//			resource.add(linkTo(methodOn(ProcessInstanceDiagramResource.class).getProcessInstanceDiagram(workDirective.getWorkflowSpecificationId(), null)).withRel("workflowSpecificationDiagram"));
		}
		return resource;
	}
}
