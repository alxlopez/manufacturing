package com.mes.modules.workMaster.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.workDirective.WorkMaster;

@SuppressWarnings("rawtypes")
@Component
public class WorkMasterResourceAssembler extends ResourceAssemblerSupport<WorkMaster, Resource> {
	@Autowired
	public EntityLinks entityLinks;

	public WorkMasterResourceAssembler() {
		super(WorkMasterResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<WorkMaster> toResource(WorkMaster workMaster) {
		Resource<WorkMaster> resource = new Resource<WorkMaster>(workMaster);
		resource.add(entityLinks.linkForSingleResource(WorkMaster.class, workMaster.getId()).slash("hierarchyScope").withRel("hierarchyScope"));		
		resource.add(entityLinks.linkForSingleResource(WorkMaster.class, workMaster.getId()).slash("parameters").withRel("parameters"));
		resource.add(entityLinks.linkForSingleResource(WorkMaster.class, workMaster.getId()).slash("materialSpecifications").withRel("materialSpecifications"));
		resource.add(entityLinks.linkForSingleResource(WorkMaster.class, workMaster.getId()).slash("parent").withRel("parent"));
		resource.add(entityLinks.linkForSingleResource(WorkMaster.class, workMaster.getId()).slash("workMasters").withRel("workMasters"));
		return resource;
	}
}
