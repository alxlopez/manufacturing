package com.mes.modules.personnel.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mes.dom.equipment.Equipment;
import com.mes.dom.personnel.QualificationTestSpecification;
import com.mes.modules.personnel.services.QualificationTestSpecificationRepository;

@RepositoryRestController
public class QualificationTestSpecificationController {
	@Autowired
	private QualificationTestSpecificationRepository repository;
	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(value = "qualificationTestSpecifications/search", method = RequestMethod.GET)
	public @ResponseBody List<QualificationTestSpecification> qualificationTestSpecificationFindByParams(
			@RequestParam(value = "id", required = false, defaultValue = "0") Long id,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "hierarchyScope", required = false) String hierarchyScope,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "location", required = false, defaultValue = "0") Long location
	) {
		QualificationTestSpecification qualificationTestSpecificationToSearch = new QualificationTestSpecification();
		if (id > 0) {
			qualificationTestSpecificationToSearch.setId(id);
		}
		if (location > 0) {
			Equipment locationToUse = new Equipment();
			locationToUse.setId(location);
			qualificationTestSpecificationToSearch.setLocation(locationToUse);
		}
		qualificationTestSpecificationToSearch.setCode(code);
		qualificationTestSpecificationToSearch.setDescription(description);
		qualificationTestSpecificationToSearch.setHierarchyScope(hierarchyScope);
		qualificationTestSpecificationToSearch.setVersion(version);
		return repository.findAll(Example.of(qualificationTestSpecificationToSearch));
	}

	@RequestMapping(value = "/qualificationTestSpecifications", method = RequestMethod.POST)
	public ResponseEntity<Resource<QualificationTestSpecification>> createQualificationTestSpecification(
			@RequestBody QualificationTestSpecification qualificationTestSpecification
	) {
		QualificationTestSpecification saved = repository.save(qualificationTestSpecification);
		Resource<QualificationTestSpecification>	resource = new Resource<QualificationTestSpecification>(saved);
		resource.add(entityLinks.linkToSingleResource(QualificationTestSpecification.class,saved.getId()).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "qualificationTestSpecifications", method = RequestMethod.PUT)
	public ResponseEntity<Resources<QualificationTestSpecification>> createBulkQualificationTestSpecification(
			@RequestBody List<QualificationTestSpecification> qualificationTestSpecifications
	) {
		List<QualificationTestSpecification> saved = repository.save(qualificationTestSpecifications);
		Resources<QualificationTestSpecification> resources = new Resources<QualificationTestSpecification>(saved);
		resources.add(entityLinks.linkToCollectionResource(Equipment.class).withSelfRel());
		return ResponseEntity.ok(resources);
	}
}
