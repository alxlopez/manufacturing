package com.mes.modules.workDirective.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkDirective;

@SuppressWarnings("rawtypes")
@Component
public class WorkDirectiveResourceAssembler extends ResourceAssemblerSupport<WorkDirective, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public WorkDirectiveResourceAssembler() {
		super(WorkDirectiveResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<WorkDirective> toResource(WorkDirective workDirective) {
		Resource<WorkDirective> resource = new Resource<WorkDirective>(workDirective);
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("hierarchyScope").withRel("hierarchyScope"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("jobOrder").withRel("jobOrder"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("parameters").withRel("parameters"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("materialSpecifications").withRel("materialSpecifications"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("parent").withRel("parent"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("workDirectives").withRel("workDirectives"));
		resource.add(entityLinks.linkForSingleResource(WorkDirective.class, workDirective.getId()).slash("workMaster").withRel("workMaster"));
		return resource;
	}
}
