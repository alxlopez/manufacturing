package com.mes.modules.workSchedule.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.workSchedule.WorkRequest;
import com.mes.modules.workSchedule.controller.WorkRequestController;

@SuppressWarnings("rawtypes")
@Component
public	class WorkRequestResourceAssembler extends ResourceAssemblerSupport<WorkRequest, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public WorkRequestResourceAssembler() {
		super(WorkRequestController.class, Resource.class);
	}

	@Override
	public Resource<WorkRequest> toResource(WorkRequest workRequest) {
		Resource<WorkRequest> resource = new Resource<WorkRequest>(workRequest);
		resource.add(entityLinks.linkToSingleResource(WorkRequest.class,workRequest.getId()).withSelfRel());
		resource.add(entityLinks.linkForSingleResource(WorkRequest.class,workRequest.getId()).slash("jobOrders").withRel("jobOrders"));
		return resource;
	}
}
