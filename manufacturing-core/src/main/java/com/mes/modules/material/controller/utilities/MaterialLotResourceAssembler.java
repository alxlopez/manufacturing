package com.mes.modules.material.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import com.mes.dom.material.MaterialLot;

@SuppressWarnings("rawtypes")
@Component
public class MaterialLotResourceAssembler extends ResourceAssemblerSupport<MaterialLot, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public MaterialLotResourceAssembler() {
		super(MaterialLotResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<MaterialLot> toResource(MaterialLot materialLot) {
		Resource<MaterialLot> resource = new Resource<MaterialLot>(materialLot);
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("materialDefinition").withRel("materialDefinition"));
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("storageLocation").withRel("storageLocation"));
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("parent").withRel("parent"));		
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("properties").withRel("properties"));
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("lots").withRel("lots"));
		resource.add(entityLinks.linkForSingleResource(MaterialLot.class, materialLot.getId()).slash("sublots").withRel("sublots"));
		return resource;
	}
}
