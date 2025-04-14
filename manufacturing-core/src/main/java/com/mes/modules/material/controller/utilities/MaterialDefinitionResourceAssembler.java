package com.mes.modules.material.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.material.MaterialDefinition;

@SuppressWarnings("rawtypes")
@Component
public class MaterialDefinitionResourceAssembler extends ResourceAssemblerSupport<MaterialDefinition, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public MaterialDefinitionResourceAssembler() {
		super(MaterialDefinitionResourceAssembler.class, Resource.class);
	}

	@Override
	public Resource<MaterialDefinition> toResource(MaterialDefinition materialDefinition) {
		Resource<MaterialDefinition> resource = new Resource<MaterialDefinition>(materialDefinition);
		resource.add(entityLinks.linkForSingleResource(MaterialDefinition.class, materialDefinition.getId()).slash("materialClasses").withRel("materialDefinition"));
		resource.add(entityLinks.linkForSingleResource(MaterialDefinition.class, materialDefinition.getId()).slash("properties").withRel("properties"));
		resource.add(entityLinks.linkForSingleResource(MaterialDefinition.class, materialDefinition.getId()).slash("parent").withRel("parent"));		
		resource.add(entityLinks.linkForSingleResource(MaterialDefinition.class, materialDefinition.getId()).slash("materialDefinitions").withRel("materialDefinitions"));
		resource.add(entityLinks.linkForSingleResource(MaterialDefinition.class, materialDefinition.getId()).slash("location").withRel("location"));
		return resource;
	}
}
