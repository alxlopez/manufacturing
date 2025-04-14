package com.mes.modules.workflowSpecification.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.WorkflowSpecification.WorkflowSpecification;

@SuppressWarnings("rawtypes")
@Component
public class WorkflowSpecificationResourceAssembler extends ResourceAssemblerSupport<WorkflowSpecification, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public WorkflowSpecificationResourceAssembler() {
		super(WorkflowSpecificationResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<WorkflowSpecification> toResource(WorkflowSpecification workflowSpecification) {
		Resource<WorkflowSpecification> resource = new Resource<WorkflowSpecification>(workflowSpecification);
		resource.add(entityLinks.linkForSingleResource(WorkflowSpecification.class, workflowSpecification.getId()).slash("nodes").withRel("nodes"));;
		resource.add(entityLinks.linkForSingleResource(WorkflowSpecification.class, workflowSpecification.getId()).slash("workDirective").withRel("workDirective"));
		return resource;
	}
}
