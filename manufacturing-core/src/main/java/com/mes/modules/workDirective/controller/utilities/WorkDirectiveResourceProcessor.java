package com.mes.modules.workDirective.controller.utilities;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.flowable.rest.service.api.runtime.process.ProcessInstanceDiagramResource;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResource;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkDirective;

@Component
public class WorkDirectiveResourceProcessor implements ResourceProcessor<Resource<WorkDirective>> {
	@Override
	public Resource<WorkDirective> process(Resource<WorkDirective> resource) {
		WorkDirective workDirective = resource.getContent();
		if (workDirective.getWorkflowSpecificationId() != null) {
			resource.add(linkTo(methodOn(ProcessInstanceResource.class).getProcessInstance(workDirective.getWorkflowSpecificationId(), null)).withRel("workflowSpecification"));
			resource.add(linkTo(methodOn(ProcessInstanceDiagramResource.class).getProcessInstanceDiagram(workDirective.getWorkflowSpecificationId(), null)).withRel("workflowSpecificationDiagram"));
		}
		return resource;
	}
}
