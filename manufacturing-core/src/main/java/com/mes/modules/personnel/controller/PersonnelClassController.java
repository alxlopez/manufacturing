package com.mes.modules.personnel.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.mes.dom.personnel.PersonnelClass;
import com.mes.modules.personnel.services.PersonnelClassRepository;

@RepositoryRestController
public class PersonnelClassController {
	@Autowired
	private PersonnelClassRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "/personnelClasses", method = RequestMethod.POST)
	public ResponseEntity<Resource<PersonnelClass>> createPersonnelClass(@RequestBody PersonnelClass personnelClass) {
		PersonnelClass saved = repository.save(personnelClass);
		Resource<PersonnelClass>	resource = new Resource<PersonnelClass>(saved);
		resource.add(entityLinks.linkToSingleResource(PersonnelClass.class,saved.getId()).withSelfRel());	
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/personnelClasses", method = RequestMethod.PUT)
	public ResponseEntity<Resources<PersonnelClass>> createBulkPersonnelClass(@RequestBody List<PersonnelClass> personnelClasses) {
		List<PersonnelClass> saved = repository.save(personnelClasses);
		Resources<PersonnelClass> resources = new Resources<PersonnelClass>(saved);
		resources.add(entityLinks.linkToCollectionResource(PersonnelClass.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}
}
