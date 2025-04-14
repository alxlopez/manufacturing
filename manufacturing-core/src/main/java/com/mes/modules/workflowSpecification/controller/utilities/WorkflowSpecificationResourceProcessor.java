package com.mes.modules.workflowSpecification.controller.utilities;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.flowable.rest.service.api.runtime.process.ProcessInstanceDiagramResource;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResource;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import com.mes.dom.WorkflowSpecification.WorkflowSpecification;

@Component
public class WorkflowSpecificationResourceProcessor implements ResourceProcessor<Resource<WorkflowSpecification>> {
	@Override
	public Resource<WorkflowSpecification> process(Resource<WorkflowSpecification> resource) {	
		WorkflowSpecification workflowSpecification = resource.getContent();
		resource.add(linkTo(methodOn(ProcessInstanceResource.class).getProcessInstance(workflowSpecification.getCode(), null)).withRel("processInstance"));
		resource.add(linkTo(methodOn(ProcessInstanceDiagramResource.class).getProcessInstanceDiagram(workflowSpecification.getCode(), null)).withRel("diagram"));
		return resource;
	}
}
