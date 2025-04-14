package com.mes.modules.personnel.controller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.mes.dom.personnel.Person;
import com.mes.modules.personnel.controller.PersonController;

@SuppressWarnings("rawtypes")
@Component
public class PersonnelResourceAssembler extends ResourceAssemblerSupport<Person, Resource> {
	@Autowired
	private EntityLinks entityLinks;

	public PersonnelResourceAssembler() {
		super(PersonController.class, Resource.class);
	}

	@Override
	public Resource<Person> toResource(Person person) {
		Resource<Person> resource = new Resource<Person>(person);
		resource.add(entityLinks.linkForSingleResource(Person.class, person.getId()).slash("properties").withRel("properties"));
		resource.add(entityLinks.linkForSingleResource(Person.class, person.getId()).slash("personnelClasses").withRel("personnelClasses"));
		resource.add(entityLinks.linkForSingleResource(Person.class, person.getId()).slash("location").withRel("location"));
		return resource;
	}
}
