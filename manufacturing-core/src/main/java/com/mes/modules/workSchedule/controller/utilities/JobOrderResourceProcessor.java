package com.mes.modules.workSchedule.controller.utilities;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

import com.mes.dom.workSchedule.JobOrder;

public class JobOrderResourceProcessor implements ResourceProcessor<Resource<JobOrder>> {
	@Override
	public Resource<JobOrder> process(Resource<JobOrder> resource) {
		return resource;
	}
}
