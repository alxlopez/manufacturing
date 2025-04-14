package com.mes.modules.workSchedule.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.workSchedule.JobOrder;
import com.mes.modules.workSchedule.controller.JobOrderController;

@SuppressWarnings("rawtypes")
@Component
public class JobOrderResourceAssembler extends ResourceAssemblerSupport<JobOrder, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public JobOrderResourceAssembler() {
		super(JobOrderController.class, Resource.class);
	}

	@Override
	public Resource<JobOrder> toResource(JobOrder jobOrder) {
		Resource<JobOrder> resource = new Resource<JobOrder>(jobOrder);
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("parameters").withRel("parameters"));
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("materialRequirements").withRel("materialRequirements"));
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("workRequest").withRel("workRequest"));
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("workMaster").withRel("workMaster"));
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("jobOrderList").withRel("jobOrderList"));
		resource.add(entityLinks.linkForSingleResource(JobOrder.class, jobOrder.getId()).slash("location").withRel("location"));
		return resource;
	}
}
